<template>
  <section>
    <div class="container">
      <v-btn @click="() => {
          releaseMemory()
          this.stop();
          this.$router.back()
      }" id="navbar">
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <bim-iot-logo id="logo" class="mx-3"></bim-iot-logo>
            <span id="projectName" style="color: #0A0046; font-size: 150%">BimIot</span>
          </div>
     </v-btn>
      <input type="file" id="file-input"/>
      <ColorPickerSensor id="colorPickers" :selectedType="this.currentSenseType"/>
      <div style="position: absolute; bottom: 0; left: 0;">
        <v-btn id="controlBtn" icon @click="play">
          <v-icon v-if="!playing">mdi-play</v-icon>
          <v-icon v-if="playing">mdi-stop</v-icon>
        </v-btn>
        <SensorsList :room_list="room_list"/>
        <TwoDToThreeDButton  @click="changeTo2d()" :state="currentPlan"/>
      </div>
      <SensorsControlButtons v-on:child-method="updateParent"/>
    </div>
<!--    v-bind:style=" {left: this.propertyPositionX + 'px' , top: this.propertyPositionY + 'px' }"-->
    <div id="properties-text" v-bind:style=" {left: this.propertyPositionX + 'px' , top: this.propertyPositionY + 'px' }">
      <p>
        ID:
        {{ entityData }}
      </p>
      <v-btn size="x-small" icon="mdi-close" variant="text" v-if='entityData !== ""' v-on:click="resetSelection()"></v-btn>
    </div>
    <div id="model"/>
  </section>
</template>

<script>
import {IfcViewerAPI, NavigationModes} from 'web-ifc-viewer';

import * as THREE from 'three';
import {MeshLambertMaterial} from 'three';
import axios from 'axios';
import sockjs from "sockjs-client/dist/sockjs"
import * as StompJs from '@stomp/stompjs';
import {
  IFCDISTRIBUTIONCONTROLELEMENT,
  IFCOPENINGELEMENT,
  IFCSENSORTYPE,
  IFCSLAB,
  IFCSPACE,
  IFCWALLSTANDARDCASE
} from 'web-ifc';
import SensorsList from './SensorsList.vue'

import SensorsControlButtons from "@/components/SensorsControlButtons";
import ColorPickerSensor from "@/components/ColorPickerSensor";

import TwoDToThreeDButton from "@/components/TwoDToThreeDButton";

import {projectStore} from "@/store/project";


import { CSS2DRenderer,CSS2DObject } from 'three/examples/jsm/renderers/CSS2DRenderer.js';

import { NavCube } from "./NavCube/NavCube";


export default {
  name: 'ModelViewer',
  props: ['token', 'projectId', 'discipline'],
  components: {
    ColorPickerSensor,
    SensorsList,
    SensorsControlButtons,
    TwoDToThreeDButton,
  },
  data() {
    return {
      receivingDataset:{},
      propertyPositionX: 10,
      propertyPositionY: 120,
      entityData: '',
      client: undefined,
      viewer: undefined,
      playing: false,
      model: undefined,
      currentSenseType: "TEMPERATURE",
      structure: undefined,
      sensorMapping: [],
      sensor_types: {},
      room_by_color: {},
      currentPlan: "3D",
      room_list: {},
      invisibleMat: new MeshLambertMaterial({
        transparent: true,
        opacity: 0.5,
        color: 0xffffff,
        depthTest: true,
        side: THREE.DoubleSide
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
      floorMesh: new MeshLambertMaterial({}),
      navCube: undefined
    }
  },
  setup() {
    const store = projectStore();
    store.fetchSensorColors();
    console.log(store.colors);
    return {store};
  },
  methods: {
    releaseMemory() {
      location.reload()
      this.viewer.dispose();
      this.viewer = null;
    },
    play() {
      if(!this.playing) {
        this.start()
      } else {
        this.stop()
      }
      this.playing = !this.playing;
    },
    async changeTo2d() {
      if(this.model == null) {
        return;
      }
      const controls = this.viewer.context.ifcCamera.cameraControls;
      if(this.currentPlan === "3D") {
        this.navCube.changeActivation(); // False
        this.viewer.IFC.loader.ifcManager.getSubset(this.model.modelID, this.floorMesh, "floor").material.visible = false;
        await this.viewer.context.ifcCamera.setNavigationMode(NavigationModes.Plan)
        await controls.reset(false);
        await this.viewer.context.ifcCamera.toggleProjection();
        await controls.setPosition(0, 50, 0, false);
        this.currentPlan = "2D"
      } else {
        this.navCube.changeActivation(); // True
        this.viewer.IFC.loader.ifcManager.getSubset(this.model.modelID, this.floorMesh, "floor").material.visible = true;
        await this.viewer.context.ifcCamera.setNavigationMode(NavigationModes.Orbit)
        await controls.reset(false);
        await controls.setPosition(0, 50, 0, false)
        await this.viewer.context.ifcCamera.toggleProjection()
        this.currentPlan = "3D"
      }
    },
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
      console.log("this is the model:",this.model)

      this.model.removeFromParent();

      const structure = await this.showStructure(viewer, this.model.modelID);
      this.structure = structure;

      const types = await viewer.IFC.getAllItemsOfType(this.model.modelID, IFCSENSORTYPE, true);
      for (let type in types) {
        this.sensor_types[types[type].Name.value] = types[type].PredefinedType.value;
      }

      const manager = this.viewer.IFC.loader.ifcManager;

      /**
       * HERE IS THE code YOU WANT IT START FROM HERE
       * */

      const floor = {
        modelID: this.model.modelID,
        ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCSLAB, false),
        removePrevious: true,
        material: this.floorMesh,
        customID: "floor"
      };
      const sensor = {
        modelID: this.model.modelID,
        ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCDISTRIBUTIONCONTROLELEMENT, false),
        material: this.sensorColor,
        removePrevious: true,
        customID: "stuff2"
      };
      const wall = {
        modelID: this.model.modelID,
        ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCWALLSTANDARDCASE, false),
        removePrevious: true,
        customID: "stuff3"
      };
      const spaces = {
        modelID: this.model.modelID,
        ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCSPACE, false),
        removePrevious: true,
        material: this.invisibleMat,
        customID: "stuff4"
      };

      let floors = await viewer.IFC.loader.ifcManager.createSubset(floor);
      let sensors = await viewer.IFC.loader.ifcManager.createSubset(sensor);
      let walls = await viewer.IFC.loader.ifcManager.createSubset(wall)
      let sp = await viewer.IFC.loader.ifcManager.createSubset(spaces);

      window.onmousemove = () => {
        if(this.viewer === null) {
          return;
        }
        this.viewer.IFC.selector.prePickIfcItem()
      };
      window.ondblclick = async (e) => {
        const {modelID, id} = await viewer.IFC.selector.pickIfcItem(true);
        const type = viewer.IFC.loader.ifcManager.getIfcType(modelID, id);
        if (type === "IFCSPACE" || type === "IFCDISTRIBUTIONCONTROLELEMENT") {
          this.propertyPositionX = e.clientX
          this.propertyPositionY = e.clientY;
          //this.entityData = this.room_list[modelID]
          this.entityData = (type === "IFCSPACE" ? "Pièce" + this.room_list[id] : "Capteur" + this.receivingDataset);
          console.log("entity data is :", this.entityData);
        } else {
          viewer.IFC.selector.unpickIfcItems(); // Unselect everything that is not room or sensor
        }
      }

      const scene = this.viewer.context.getScene();
      scene.add(floors);
      scene.add(sensors);
      scene.add(walls);
      scene.add(sp);

      await this.changeColor(this.room_list, manager, this.currentSenseType);
      await this.getSensors(structure, manager, this.model.modelID);
      this.sendMapping();
    },
    calculateCenter(mesh){
      const coordinates = [];
      const alreadySaved = new Set();
      const position = mesh.geometry.attributes.position;
      for(let index of mesh.geometry.index.array) {
        if(!alreadySaved.has(index)){
          coordinates.push(position.getX(index));
          coordinates.push(position.getY(index));
          coordinates.push(position.getZ(index));
          alreadySaved.add(index);
        }
      }
      const vertices = Float32Array.from(coordinates);

      const verticex = [];
      for (let i = 0; i < vertices.length; i += 3) {
        const vertex = [vertices[i], vertices[i + 1], vertices[i + 2]];
        verticex.push(vertex);
      }

      let minX = Number.POSITIVE_INFINITY;
      let minY = Number.POSITIVE_INFINITY;
      let minZ = Number.POSITIVE_INFINITY;
      let maxX = Number.NEGATIVE_INFINITY;
      let maxY = Number.NEGATIVE_INFINITY;
      let maxZ = Number.NEGATIVE_INFINITY;

      for (const vertex of verticex) {
        minX = Math.min(minX, vertex[0]);
        minY = Math.min(minY, vertex[1]);
        minZ = Math.min(minZ, vertex[2]);
        maxX = Math.max(maxX, vertex[0]);
        maxY = Math.max(maxY, vertex[1]);
        maxZ = Math.max(maxZ, vertex[2]);
      }

      const centerX = (minX + maxX) / 2;
      const centerY = (minY + maxY) / 2;
      const centerZ = (minZ + maxZ) / 2;

      return [centerX, centerY, centerZ];
    },
    creatLabel(labelDivId,position, initContent, className){
      const labelDiv = document.createElement( 'div' );
      labelDiv.id = labelDivId;
      labelDiv.className = className;
      labelDiv.textContent = initContent;
      labelDiv.style.marginTop = '-1em'

      const label = new CSS2DObject(labelDiv);
      label.position.set(position[0], position[1], position[2])
      label.layers.set(0)
      return label
    },
    modifyTextContentLabel(label,newContent){
      label.element.textContent = newContent;
    },
    subscribe: function (greeting) {

      const response = greeting;

      console.log("subscribe response :", response);


      if(response["sensorType"] === "END"){
        alert("Simulation terminate");
        this.play();
      }

      if (this.model === undefined || !(response["roomIfcID"] in this.room_list || response["color"] === undefined)) {
        return;
      }
      this.receivingDataset = this.room_list[response["roomIfcID"]][response["sensorType"]]
      // Update last recorded value for this sensor
      for (let sensor in this.room_list[response["roomIfcID"]][response["sensorType"]]) {
        if (this.room_list[response["roomIfcID"]][response["sensorType"]][sensor].IFCid === response["sensorIfcID"]) {
          this.room_list[response["roomIfcID"]][response["sensorType"]][sensor].value = response["value"];
        }
      }
      console.log("Room List is :",this.room_list)
      console.log("Updated room is :",this.room_list[response["roomIfcID"]][response["sensorType"]])

      let mesh = new MeshLambertMaterial({
        transparent: true,
        opacity: 0.5,
        color: new THREE.Color(response["color"]).getHex(),
        depthTest: true,
        side: THREE.DoubleSide
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
    async changeColor(room_ids, manager, sensorType) {
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
    updateParent: async function (type) {
      this.currentSenseType = type

      const manager = this.viewer.IFC.loader.ifcManager;
      switch (type) {
        case 'HUMIDITY':
          this.removeAll(this.room_list, manager)
          await this.changeColor(this.room_list, manager, type);
          break;
        case 'LIGHT':
          this.removeAll(this.room_list, manager)
          await this.changeColor(this.room_list, manager, type);
          break;
        case 'CO2':
          this.removeAll(this.room_list, manager)
          await this.changeColor(this.room_list, manager, type);
          break;
        case "TEMPERATURE":
          this.removeAll(this.room_list, manager)
          await this.changeColor(this.room_list, manager, type);
          break;
        default:
          console.log("Unknown type!");
      }
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
      window.addEventListener("beforeunload", this.beforeUnloadListener, {capture: true});
      axios.put(`/api/bimiot/start/${this.store.currentProjectName}`, {})
    },
    stop: function () {
      window.removeEventListener("beforeunload", this.beforeUnloadListener, {capture: true});
      axios.put(`/api/bimiot/stop/${this.store.currentProjectName}`, {});
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
    },
    resetSelection: function() {
      this.entityData = "";
      this.viewer.IFC.selector.unpickIfcItems();
    },
    beforeUnloadListener: function(event) {
      this.stop();
      event.preventDefault();
      return event.returnValue = '';
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
  },

  unmounted() {
    this.releaseMemory()
    this.stop();
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
          const file = changed.target.files[0];
          const ifcURL = URL.createObjectURL(file);
          const model = await viewer.IFC.loadIfcUrl(ifcURL);
          this.model = model;
          model.removeFromParent();

          const structure = await this.showStructure(viewer, model.modelID);
          this.structure = structure;

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

          //viewer.context.scene.add(floorLabel);
          //
          // const labelRenderer = new CSS2DRenderer();
          // //labelRenderer.setSize( window.innerWidth, window.innerHeight );
          // labelRenderer.domElement.style.position = 'absolute';
          // labelRenderer.domElement.style.top = '0px';
          // document.body.appendChild( labelRenderer.domElement );
          //
          // console.log("Here")
          // console.log("floor label render: ", labelRenderer)


          const floor = {
            modelID: this.model.modelID,
            ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCSLAB, false),
            removePrevious: true,
            material: this.floorMesh,
            customID: "floor"
          };

          let sensorList = await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID, IFCDISTRIBUTIONCONTROLELEMENT, false);
          const oneSensor = sensorList[1];
          const sensor = {
            modelID: model.modelID,
            ids: [oneSensor],
            material: this.sensorColor,
            removePrevious: true,
            customID: "sensor",
            applyBVH : true
          }

          const wall = {
            modelID: model.modelID,
            ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID, IFCWALLSTANDARDCASE, false),
            removePrevious: true,
            customID: "stuff3"
          }
          let spaceList =  await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID, IFCSPACE, false)
          const oneSpace = spaceList[0]
          const spaces = {
            modelID: model.modelID,
            ids:[oneSpace],
            removePrevious: true,
            material: this.invisibleMat,
            customID: "stuff4",
            applyBVH : true
          }

          let floors = await viewer.IFC.loader.ifcManager.createSubset(floor);
          let sensors = await viewer.IFC.loader.ifcManager.createSubset(sensor);
          let walls = await viewer.IFC.loader.ifcManager.createSubset(wall);
          let sp = await viewer.IFC.loader.ifcManager.createSubset(spaces);
          sp.geometry.computeBoundingSphere();
          //const sensorBox = new THREE.Box3().setFromObject(myMesh)

          sensors.geometry.computeBoundingSphere();

          const center = this.calculateCenter(sp)

          //console.log(center, "centerrrr")


          //console.log('coordinate',vertices)


//############################### space Label

          const spaceDiv = document.createElement( 'div' );
          spaceDiv.className = 'label';
          spaceDiv.textContent = 'Space';
          spaceDiv.style.marginTop = '-1em'

          const spLabel = new CSS2DObject(spaceDiv);
          console.log(sp.geometry.boundingBox, "hellooo");
          spLabel.position.set(center[0], center[1],center[2])
          spLabel.layers.set(0)
          //sp.add(spLabel)
          console.log("space label 3D object", spLabel)

          //############ Sensor Label

          //const sensorPosition = sensors.geometry.attributes.position
          // const sensorDiv = document.createElement( 'div' );
          // sensorDiv.id = '1';
          // sensorDiv.className = 'label';
          // sensorDiv.textContent = 'Sensor';
          // sensorDiv.style.marginTop = '-1em'
          //
          // const sensorLabel = new CSS2DObject(sensorDiv);
          //
          // sensorLabel.position.set(center[0], center[1], center[2])
          // sensorLabel.layers.set(0)
          // sensors.add(sensorLabel)
          // console.log("sensor label 3D object", sensorLabel)

          const label = this.creatLabel("label1",center,"space","label")
          sp.add(label)
          this.modifyTextContentLabel(label,"new space")

          const scene = this.viewer.context.getScene();
          scene.add(floors);
          scene.add(sensors);
          scene.add(walls);
          scene.add(sp);
        },

        false
    );
    viewer.container = container;
    const navCube = new NavCube(viewer);
    navCube.onPick(this.model);
    this.navCube = navCube;

  },
}
</script>

<style>
#model {
  position: absolute;
  left: 0;
  top: 0;
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
  display:flex;
  position: absolute;
  align-items: center;
  /*left: v-bind(propertyPositionX)px;*/
  /*top: v-bind(propertyPositionY)px;*/
  /*left: 601px;*/
  /*top: 330px;*/
}

#colorPickers {
  position: absolute !important;
}

#navbar {
  top: 0;
  border-radius: 0 0 25px 0;
  background-color: #888888;
  elevation: 3deg;
  position: absolute;
  height: 5em;
  width: 12em;
}

#projectName{
 position: absolute;
 right: 10px;

}

#logo {
  position: absolute;
  left:0;
}

.v-application__wrap {
  min-height: auto;
}

#controlBtn{
  bottom: 160px;
  left: 35px;
  color: white;
  background-color: #0A0046;
}

.container {
  margin-top: 80px;
}
</style>
