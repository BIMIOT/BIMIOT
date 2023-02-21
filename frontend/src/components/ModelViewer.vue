<template>
  <section>
    <div class="container">
      <v-app>
        <v-app-bar color="grey" :elevation="2">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <bim-iot-logo class="mx-3"></bim-iot-logo>
            <span style="color: #0A0046; font-size: 150%">BimIot</span>
          </div>
        </v-app-bar>

      </v-app>
      <input type="file" id="file-input"/>
      <v-btn id="play" v-on:click="start()">Play</v-btn>
      <v-btn id="stop" v-on:click="stop()">Stop</v-btn>
      <ColorPickerSensor id="colorPickers"/>
      <div  style="position: absolute; bottom: 0; left: 0;">
        <SensorsList :room_list="room_list"/>
      </div>
      <SensorsControlButtons v-on:child-method="updateParent"/>
    </div>

    <p id="properties-text">
      ID:
      {{ entityData }}
    </p>
    <div id="model"/>
  </section>
</template>

<script>
import {IfcViewerAPI} from 'web-ifc-viewer';
import {MeshLambertMaterial} from 'three';
import axios from 'axios';
import sockjs from "sockjs-client/dist/sockjs"
import * as StompJs from '@stomp/stompjs';
import {
  IFCSPACE,
  IFCSLAB,
  IFCOPENINGELEMENT,
  IFCDISTRIBUTIONCONTROLELEMENT,
  IFCWALLSTANDARDCASE,
  IFCSENSORTYPE,
  IFCSENSOR
} from 'web-ifc';
import SensorsList from './SensorsList.vue'

import * as THREE from 'three';


import SensorsControlButtons from "@/components/SensorsControlButtons";
import ColorPickerSensor from "@/components/ColorPickerSensor";
import {projectStore} from "@/store/project";

export default {
  name: 'ModelViewer',
  props: ['token', 'projectId', 'discipline'],
  components: {
    ColorPickerSensor,
    SensorsList,
    SensorsControlButtons,
  },

  data() {
    return {
      entityData: '',
      client: undefined,
      viewer: undefined,
      model: undefined,
      currentSenseType: "TEMPERATURE",
      structure: undefined,
      sensorMapping: [], // roomId:[{IFCsensorId:"1",DatasetId:"1"}]
      sensor_types: {},
      room_by_color: {},
      room_list: {}, // roomId:{type:[IFCid:"val", DataId:"val", value:"val"]}
      invisibleMat: new MeshLambertMaterial({
        transparent: true,
        opacity: 0.4,
        color: 0xffffff,
        depthTest: false,
      }),
      preSelectMat: new MeshLambertMaterial({
        transparent: true,
        opacity: 0.3,
        color: 0xff0000,
        depthTest: false,
      }),
      sensorColor: new MeshLambertMaterial({
        transparent: false,
        opacity: 1,
        color: 0xfcba03,
      }),
      preSelectMatBlue: new MeshLambertMaterial({
        transparent: true,
        opacity: 0.3,
        color: 0x00FFFF,
        depthTest: false,
      }),
      currentColorRange: []
    }
  },
  setup() {
    const store = projectStore();
    return {store};
  },
  methods: {
    moveComponentToSubDiv() {
      const subContainer = document.getElementById('sub-container');
      const childComponent =  document.getElementById('model');
      subContainer.appendChild(childComponent);
    },
    async loadFile(viewer) {
      const response = await axios.get(`/api/bimiot/simulation/files/${this.store.currentProjectName}`, {
        responseType: 'blob',
      });
      const ifcURL = URL.createObjectURL(response.data);
      this.model = await this.viewer.IFC.loadIfcUrl(ifcURL);

      this.model.removeFromParent();

      /*
      this.model = model;
      model.removeFromParent();
      */

      const structure = await this.showStructure(viewer, this.model.modelID);
      this.structure = structure;


      //const spaces = await viewer.IFC.getAllItemsOfType(model.modelID, IFCSPACE, true);

      const types = await viewer.IFC.getAllItemsOfType(this.model.modelID, IFCSENSORTYPE, true);
      for (let type in types) {
        this.sensor_types[types[type].Name.value] = types[type].PredefinedType.value;
      }

      const manager = this.viewer.IFC.loader.ifcManager;
      await this.getSensors(structure, manager, this.model.modelID);
      this.sendMapping();

      /**
       * HERE IS THE code YOU WANT IT START FROM HERE
       * */

      const floor = {
        modelID: this.model.modelID,
        ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCSLAB, false),
        removePrevious: true,
        customID: "stuff"
      }
      const sensor = {
        modelID: this.model.modelID,
        ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCDISTRIBUTIONCONTROLELEMENT, false),
        material: this.sensorColor,
        removePrevious: true,
        customID: "stuff2"
      }

      const wall = {
        modelID: this.model.modelID,
        ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCWALLSTANDARDCASE, false),
        removePrevious: true,
        customID: "stuff3"
      }


      const spaces = {
        modelID: this.model.modelID,
        ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCSPACE, false),
        removePrevious: true,
        material: this.invisibleMat,
        customID: "stuff4"
      }

      var floors = await viewer.IFC.loader.ifcManager.createSubset(floor);
      var sensors = await viewer.IFC.loader.ifcManager.createSubset(sensor);
      var walls = await viewer.IFC.loader.ifcManager.createSubset(wall)
      var sp = await viewer.IFC.loader.ifcManager.createSubset(spaces);


      const scene = this.viewer.context.getScene();
      scene.add(floors);
      scene.add(sensors);
      scene.add(walls);

      this.changeColor(this.room_list, manager, this.currentSenseType)

    },
    subscribe: function (greeting) {

      const response = greeting;
      if (this.model === undefined || !(response["roomIfcID"] in this.room_list || response["color"] === undefined)) {
        return;
      }

      // Update last recorded value for this sensor
      for (let sensor in this.room_list[response["roomIfcID"]][response["sensorType"]]) {
        if (this.room_list[response["roomIfcID"]][response["sensorType"]][sensor].IFCid === response["sensorIfcID"]) {
          this.room_list[response["roomIfcID"]][response["sensorType"]][sensor].value = response["value"];
        }
      }

      let mesh = new MeshLambertMaterial({
        transparent: true,
        opacity: 0.3,
        color: new THREE.Color(response["color"]).getHex(),
        depthTest: false,
      });

      const manager = this.viewer.IFC.loader.ifcManager;
      if (this.room_by_color[response["roomIfcID"]] === undefined) {
        this.room_by_color[response["roomIfcID"]] = {[response["sensorType"]]: mesh};
      } else {
        manager.removeSubset(this.model.modelID, this.room_by_color[response["roomIfcID"]][response["sensorType"]], response["roomIfcID"] + response["sensorType"] + "");
        this.room_by_color[response["roomIfcID"]][response["sensorType"]] = mesh;
      }


      if (response["sensorType"] === this.currentSenseType && this.room_by_color[response["roomIfcID"]][this.currentSenseType] !== this.invisibleMat) {
        manager.createSubset({
          modelID: this.model.modelID,
          ids: [response["roomIfcID"]],
          material: this.room_by_color[response["roomIfcID"]][this.currentSenseType],
          scene: this.viewer.context.getScene(),
          removePrevious: false,
          customID: response["roomIfcID"] + response["sensorType"] + ""
        });
      }
    },
    convertHexToInt: function (colors) {
      return colors.map(color => {
        var color2 = new THREE.Color(color.value);
        return new MeshLambertMaterial({
          transparent: true,
          opacity: 0.3,
          color: color2.getHex(),
          depthTest: false,
        });
      });
    },
    removeAll: function (room_ids, manager) {
      const room_ids_iter = Object.keys(room_ids);
      const sensorTypes = ["TEMPERATURE", "HUMIDITY", "LIGHT", "CO2"];
      for (const sensorType of sensorTypes) {
        for (const id of room_ids_iter) {
          manager.removeSubset(this.model.modelID,
              this.room_by_color[parseInt(id, 10)] === undefined ?
                  this.invisibleMat : this.room_by_color[parseInt(id, 10)][sensorType]
              , id + sensorType + "");
        }
      }
    },
    changeColor: function (room_ids, manager, sensorType) {
      const room_ids_iter = Object.keys(room_ids);

      for (const id of room_ids_iter) {
        manager.createSubset({
          modelID: this.model.modelID,
          ids: [parseInt(id, 10)],
          material: this.room_by_color[parseInt(id, 10)] === undefined ? this.invisibleMat : this.room_by_color[parseInt(id, 10)][sensorType],
          scene: this.viewer.context.getScene(),
          removePrevious: false,
          customID: id + sensorType + ""
        });
      }
    },
    updateParent: function (type) {
      this.currentSenseType = type

      const manager = this.viewer.IFC.loader.ifcManager;
      switch (type) {
        case 'HUMIDITY':
          this.removeAll(this.room_list, manager)
          this.changeColor(this.room_list, manager, type);
          this.currentColorRange = this.humMeshes;
          break;
        case 'LIGHT':
          this.removeAll(this.room_list, manager)
          this.changeColor(this.room_list, manager, type);
          this.currentColorRange = this.lumMeshes;
          break;
        case 'CO2':
          this.removeAll(this.room_list, manager)
          this.changeColor(this.room_list, manager, type);
          this.currentColorRange = this.co2Meshes;
          break;
        case "TEMPERATURE":
          this.removeAll(this.room_list, manager)
          this.changeColor(this.room_list, manager, type);
          this.currentColorRange = this.tempMeshes;
          break;
        default:
          console.log("Unknown type!");
      }
    },
    newSubsetOfType: async function (viewer, category) {
      const manager = viewer.IFC.loader.ifcManager;
      const ids = await manager.getAllItemsOfType(0, category, false);

      return manager.createSubset({
        modelID: 0,
        scene: this.viewer.context.getScene(),
        ids: [722],
        applyBVH: true,
        removePrevious: true,
        customID: category.toString(),
      });
    },
    showStructure: async function (viewer, modelID) {
      const manager = viewer.IFC.loader.ifcManager;
      const relIDs = await manager.getSpatialStructure(modelID);
      return relIDs;
    },
    getSensors: async function (relIDs, manager, modelID) {
      if (relIDs.type === "IFCSPACE") {
        this.room_list[relIDs.expressID] = {};
        this.sensorMapping.push({"roomId": relIDs.expressID, "sensors": []});
      }
      for (let component in relIDs.children) {
        if (relIDs.type === "IFCSPACE" && relIDs.children[component].type === "IFCDISTRIBUTIONCONTROLELEMENT") {
          const sensor = await manager.getItemProperties(modelID, relIDs.children[component].expressID);
          const type_name = this.fromIfcType(this.sensor_types[sensor.ObjectType.value]);
          if (this.room_list[relIDs.expressID][type_name] === undefined) {
            this.room_list[relIDs.expressID][type_name] = [];
          }
          this.room_list[relIDs.expressID][type_name].push({
            IFCid: relIDs.children[component].expressID,
            DataId: sensor.ObjectType.value.split(":")[0],
            value: undefined
          });
          this.sensorMapping[this.sensorMapping.length - 1].sensors.push({
            "sensorIFCid": relIDs.children[component].expressID,
            "sensorDataSetId": sensor.ObjectType.value.split(":")[0],
            "type": type_name,
            "value": undefined
          });
        }
        await this.getSensors(relIDs.children[component], manager, modelID);
      }
    },
    start: function () {
      axios.put(`/api/bimiot/start/${this.store.currentProjectName}`, {})
    },
    stop: function () {
      axios.put(`/api/bimiot/stop/${this.store.currentProjectName}`, {})
    },
    sendMapping: function () {
      let config = {
        headers: {
          'Content-Type': 'application/json',
        }
      }
      axios.post("/api/bimiot/mapping", JSON.stringify(this.sensorMapping), config);
    },
    fromIfcType: function (ifcType) {
      switch (ifcType) {
        case "TEMPERATURESENSOR":
          return "TEMPERATURE";
        case "HUMIDITYSENSOR":
          return "HUMIDITY";
        case "CO2SENSOR":
          return "CO2";
        case "LIGHTSENSOR":
          return "LIGHT";
        default:
          return undefined;
      }
    }
  },
  created: function () {
    let client = new StompJs.Client({
      brokerURL: 'ws://localhost:80/sensors-data-endpoint',
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    // Fallback code
    if (typeof WebSocket !== 'function') {
      // For SockJS you need to set a factory that creates a new SockJS instance
      // to be used for each (re)connect
      client.webSocketFactory = function () {
        // Note that the URL is different from the WebSocket URL
        return new sockjs('/sensors-data-endpoint');
      };
    }

    client.onConnect = (frame) => {
      // Do something, all subscribes must be done is this callback
      // This is needed because this will be executed after a (re)connect
      client.subscribe('/data/sensors', (greeting) => {
        const response = JSON.parse(greeting.body);

        this.subscribe(response);

      });

    };

    client.onStompError = function (frame) {
      // Will be invoked in case of error encountered at Broker
      // Bad login/passcode typically will cause an error
      // Complaint brokers will set `message` header with a brief message. Body may contain details.
      // Compliant brokers will terminate the connection after any error
    };

    client.activate();
    this.client = client;

    const changeColor = (relIDs, roomId, sensorId, material, previousMaterial, groupId) => {
      const manager = this.viewer.IFC.loader.ifcManager;
      manager.removeSubset(this.model.modelID, previousMaterial, groupId);

      manager.createSubset({
        modelID: this.model.modelID,
        ids: [roomId],
        material: material,
        scene: this.viewer.context.getScene(),
        removePrevious: false,
        customID: groupId
      });
    }
  },

  unmounted() {
    this.viewer.dispose();
    this.store.currentProjectName = null;
  },

  mounted() {
    this.moveComponentToSubDiv()
    const container = document.getElementById('model');
    const viewer = new IfcViewerAPI({container});
    this.viewer = viewer;
    viewer.axes.setAxes();
    viewer.grid.setGrid();
    viewer.IFC.setWasmPath('../../IFCjs/');
    viewer.IFC.loader.ifcManager.parser.setupOptionalCategories({
      [IFCSPACE]: true,
      [IFCOPENINGELEMENT]: false
    });

    this.loadFile(viewer);
    const input = document.getElementById("file-input");

    input.addEventListener("change",

        async (changed) => {
          //await ifcapi.Init();
          const file = changed.target.files[0];
          const ifcURL = URL.createObjectURL(file);
          const model = await viewer.IFC.loadIfcUrl(ifcURL);
          this.model = model;

          model.removeFromParent();

          /*
          this.model = model;
          model.removeFromParent();
          */

          const structure = await this.showStructure(viewer, model.modelID);
          this.structure = structure;


          //const spaces = await viewer.IFC.getAllItemsOfType(model.modelID, IFCSPACE, true);

          const types = await viewer.IFC.getAllItemsOfType(model.modelID, IFCSENSORTYPE, true);
          for (let type in types) {
            this.sensor_types[types[type].Name.value] = types[type].PredefinedType.value;
          }

          const manager = this.viewer.IFC.loader.ifcManager;
          await this.getSensors(structure, manager, model.modelID);
          this.sendMapping();


          /**
           * HERE IS THE code YOU WANT IT START FROM HERE
           * */

          const floor = {
            modelID: model.modelID,
            ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID, IFCSLAB, false),
            removePrevious: true,
            customID: "stuff"
          }

          const sensor = {
            modelID: model.modelID,
            ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID, IFCDISTRIBUTIONCONTROLELEMENT, false),
            material: this.sensorColor,
            removePrevious: true,
            customID: "stuff2"
          }

          const wall = {
            modelID: model.modelID,
            ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID, IFCWALLSTANDARDCASE, false),
            removePrevious: true,
            customID: "stuff3"
          }


          const spaces = {
            modelID: model.modelID,
            ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID, IFCSPACE, false),
            removePrevious: true,
            material: this.invisibleMat,
            customID: "stuff4"
          }

          var floors = await viewer.IFC.loader.ifcManager.createSubset(floor);
          var sensors = await viewer.IFC.loader.ifcManager.createSubset(sensor);
          var walls = await viewer.IFC.loader.ifcManager.createSubset(wall)
          var sp = await viewer.IFC.loader.ifcManager.createSubset(spaces);


          const scene = this.viewer.context.getScene();
          scene.add(floors);
          scene.add(sensors);
          scene.add(walls);


        },

        false
    );
  },
}
</script>

<style>
#model {
  position: absolute;
  left: 0%;
  top: 0%;
  width: 100% !important;
  height: 100% !important;
}

#file-input {
  position: relative;
  /*left: 10%;*/
  /*top: 10%;*/
}

#play {
  position: relative;
  color: blue;
  margin: 0.5em 0.5em 0.5em;
}

#stop {
  position: relative;
  color: blue;
  margin: 0.5em 0.5em 0.5em;
}

#properties-text {
  position: absolute;
  left: 0%;
  bottom: 0%;
}

#colorPickers {
  position: absolute !important;
}

.v-application__wrap {
  min-height: auto;
}

.container {
  margin-top: 80px;
}
</style>
