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
      <transition name="fade" mode="out-in">
      <div   id="progress-bar" >
        {{knowledge}} %
      </div>
      </transition>
      <div style="position: absolute; bottom: 0; left: 0;" class="align-items-center">
        <div>
          <v-btn id="stopBtn" icon="mdi-stop" :disabled="!inSimulation" @click="stop"/>
        </div>
        <div>
          <v-btn id="playBtn" icon @click="play">
            <v-icon v-if="!playing">mdi-play</v-icon>
            <v-icon v-if="playing">mdi-pause</v-icon>
          </v-btn>
        </div>
        <div>
          <SensorsList :room_list="room_list"/>
        </div>
        <div>
          <TwoDToThreeDButton  @click="changeTo2d()" :state="currentPlan"/>
        </div>
      </div>
      <SensorsControlButtons v-on:child-method="updateParent"/>
    </div>

    <div id="properties-text">
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
import {roomsStateStore} from "@/store/rooms";
import {CSS2DObject } from 'three/examples/jsm/renderers/CSS2DRenderer.js';

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
      entityData: '',
      knowledge: 0,
      roomIdToMesh: {},
      arrayOfKids: [],
      client: undefined,
      viewer: undefined,
      playing: false,
      model: undefined,
      currentSenseType: "TEMPERATURE",
      structure: undefined,
      sensorMapping: [],
      space_list: {},
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
      navCube: undefined,
      inSimulation: false
    }
  },
  setup() {
    const store = projectStore();
    const roomStore = roomsStateStore()
    store.fetchSensorColors();
    return {store,roomStore};
  },
  watch: {
    arrayOfKids: {
      handler(val, oldVal) {
        const room_ids_iter = Object.keys(this.room_list);
        let value = Math.trunc((val.length*100)/room_ids_iter.length);
        if(value > 100) {
          this.knowledge = 100
          document.getElementById("progress-bar").style.visibility = "hidden";
          document.getElementById("model").style.filter = "blur(0px)";
        } else  {
          this.knowledge = Math.trunc((val.length*100)/room_ids_iter.length);
        }
      },
      deep: true
    }
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
        this.pause()
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
        await controls.setPosition(0, 1, 0, false);
        this.currentPlan = "2D"
      } else {
        this.navCube.changeActivation(); // True
        this.viewer.IFC.loader.ifcManager.getSubset(this.model.modelID, this.floorMesh, "floor").material.visible = true;
        await this.viewer.context.ifcCamera.setNavigationMode(NavigationModes.Orbit)
        await controls.reset(false);
        await controls.setPosition(0, 1, 0, false)
        await this.viewer.context.ifcCamera.toggleProjection()
        this.currentPlan = "3D"
      }
    },
    moveComponentToSubDiv() {
      const subContainer = document.getElementById('sub-container');
      const childComponent =  document.getElementById('model');
      subContainer.appendChild(childComponent);
    },
    async getMappingAndLoad(structure, manager) {
      await this.getSensors(structure, manager, this.model.modelID);
      await new Promise((resolve, reject) => {
        this.createAllSubsets(this.room_list);
        resolve();
      });
    },
    async loadFile() {
      const response = await axios.get(`/api/bimiot/simulation/files/${this.store.currentProjectName}`, {
        responseType: 'blob',
      });
      const ifcURL = URL.createObjectURL(response.data);
      this.model = await this.viewer.IFC.loadIfcUrl(ifcURL,true);

      this.model.removeFromParent();

      const structure = await this.showStructure(this.viewer, this.model.modelID);
      this.structure = structure;

      const types = await this.viewer.IFC.getAllItemsOfType(this.model.modelID, IFCSENSORTYPE, true);
      for (let type in types) {
        this.sensor_types[types[type].Name.value] = types[type].PredefinedType.value;
      }

      const manager = this.viewer.IFC.loader.ifcManager;

      const floor = {
        modelID: this.model.modelID,
        ids: await this.viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCSLAB, false),
        removePrevious: true,
        material: this.floorMesh,
        customID: "floor"
      };
      const sensor = {
        modelID: this.model.modelID,
        ids: await this.viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCDISTRIBUTIONCONTROLELEMENT, false),
        material: this.sensorColor,
        removePrevious: true,
        customID: "stuff2"
      };
      const wall = {
        modelID: this.model.modelID,
        ids: await this.viewer.IFC.loader.ifcManager.getAllItemsOfType(this.model.modelID, IFCWALLSTANDARDCASE, false),
        removePrevious: true,
        customID: "stuff3"
      };

      let floors = await this.viewer.IFC.loader.ifcManager.createSubset(floor);
      let sensors = await this.viewer.IFC.loader.ifcManager.createSubset(sensor);
      let walls = await this.viewer.IFC.loader.ifcManager.createSubset(wall)

      window.onmousemove = () => {
        if(this.viewer === null) {
          return;
        }
        this.viewer.IFC.selector.prePickIfcItem()
      };

      window.ondblclick = async () => {
        const {modelID, id} = await this.viewer.IFC.selector.pickIfcItem(true);
        const type = this.viewer.IFC.loader.ifcManager.getIfcType(modelID, id);
        if (type === "IFCSPACE" || type === "IFCDISTRIBUTIONCONTROLELEMENT") {
          this.entityData = (type === "IFCSPACE" ? "Pièce" : "Capteur") + " - " + id;
        } else {
          this.viewer.IFC.selector.unpickIfcItems(); // Unselect everything that is not room or sensor
        }
      }

      const scene = this.viewer.context.getScene();
      scene.add(floors);
      scene.add(sensors);
      scene.add(walls);

      //await this.getMappingAndLoad(structure, manager);
      await this.getSensors(structure, manager, this.model.modelID);
      this.sendMapping();
    },
    calculateCenter(subset){
      subset.geometry.computeBoundingSphere()
      const coordinates = [];
      const alreadySaved = new Set();
      const position = subset.geometry.attributes.position;
      for(let index of subset.geometry.index.array) {
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
      const centerY = minY;
      const centerZ = (minZ + maxZ) / 2;

      return [centerX, centerY, centerZ];
    },
    createLabel(position, initContent, className, roomId){
      const labelDiv = document.createElement( 'div' );
      labelDiv.id = roomId;
      labelDiv.className = className;
      labelDiv.textContent = initContent;
      labelDiv.style.marginTop = '-1em'
      const label = new CSS2DObject(labelDiv);
      label.position.set(position[0], position[1], position[2])
      label.layers.set(0)
      //console.log("label is :",label)
      return label
    },
    modifyTextContent(roomId,newContent){
      const labelDiv = document.getElementById(roomId)
      labelDiv.textContent = newContent
    },
    subscribe: function (greeting) {

      const manager = this.viewer.IFC.loader.ifcManager;

      const response = greeting;

      if(response["sensorType"] === "END") {
        alert("Simulation terminate");
        this.play();
        this.inSimulation = false;
      }



      if (this.model === undefined || !(response["roomIfcID"] in this.room_list)) {

         return;
      }
      console.log("im called")

      this.roomStore.storeNewRoomColorByType(response["roomIfcID"],response["sensorType"],response["color"]);

      if(this.space_list[response["roomIfcID"]] === undefined){
        this.space_list[response["roomIfcID"]] = {};
      }
      this.space_list[response["roomIfcID"]][response["sensorType"]] = response["averageValue"];
      for (let sensor in this.room_list[response["roomIfcID"]][response["sensorType"]]) {
        if (this.room_list[response["roomIfcID"]][response["sensorType"]][sensor].IFCid === response["sensorIfcID"]) {
          this.room_list[response["roomIfcID"]][response["sensorType"]][sensor].value = response["value"];
        }
      }

      if (response["sensorType"] === this.currentSenseType) {
        const roomMesh =  this.roomIdToMesh[response["roomIfcID"]];
        let room = manager.getSubset(this.model.modelID,roomMesh,response["roomIfcID"]);
        console.log(room, "i got here but something worng")
        room.material.color.set(response["color"])
        this.modifyTextContent(response["roomIfcID"], response["averageValue"])
      }
    },
    convertHexToInt: function (colors) {
      return colors.map(color => {
        let color2 = new THREE.Color(color.value);
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
    createAllSubsets: function (room_ids) {
      const manager = this.viewer.IFC.loader.ifcManager;
      const room_ids_iter = Object.keys(room_ids);
      let subsets = []

      for (const id of room_ids_iter) {
        let mesh = new MeshLambertMaterial({
          transparent: true,
          opacity: 0.4,
          color: 0xffffff,
          side: THREE.DoubleSide,
          depthTest: true,
        })

        this.roomIdToMesh[parseInt(id, 10)] = mesh;

       let subset = manager.createSubset({
          modelID: this.model.modelID,
          ids: [parseInt(id, 10)],
          material: mesh,

          removePrevious: false,
          customID: id
        });

        const center= this.calculateCenter(subset)
        const label = this.createLabel(center,"","spaceLabel",id);
        subset.add(label)
        subsets.push(subset)

        console.log("hello")
      }
      console.log("started to add subset to scene");
      let i = 1;
      for (const subset of subsets) {
        setTimeout(() => {
          console.log("add one : ", this.knowledge);
          this.viewer.context.getScene().add(subset);
          this.arrayOfKids = this.viewer.context.getScene().children.filter(obj => obj.material && obj.material.type !== undefined && obj.material.type === "MeshLambertMaterial")
        }, i*500);
        i++;
      }
      console.log("i finished")
    },
    async changeColor(room_ids, manager, sensorType) {
      const room_ids_iter = Object.keys(room_ids);
      for (const id of room_ids_iter) {
        const color = this.roomStore.getLastRoomColorByType(id,sensorType);
        const roomMesh = this.roomIdToMesh[id];
        let subset = manager.getSubset(this.model.modelID,roomMesh,id);
        if(!color) {
          subset.material.color.set(0xffffff);
        } else {
          subset.material.color.set(color);
        }
      }
    },
    async changeLabelContent(room_ids,sensorType){
      const room_ids_iter = Object.keys(room_ids);
      let  newContent;
      for (const id of room_ids_iter){
        if(this.space_list[id] === undefined || this.space_list[id][sensorType] === undefined){
          newContent = ""
        }else {
          newContent = this.space_list[id][sensorType];
        }
        this.modifyTextContent(id, newContent);
      }
    },
    updateParent: async function (type) {
      this.currentSenseType = type

      const manager = this.viewer.IFC.loader.ifcManager;


      switch (type) {
        case 'HUMIDITY':
          await this.changeColor(this.room_list, manager, type);
          await this.changeLabelContent(this.room_list,type);
          break;
        case 'LIGHT':
          await this.changeColor(this.room_list, manager, type);
          await this.changeLabelContent(this.room_list,type);
          break;
        case 'CO2':
          await this.changeColor(this.room_list, manager, type);
          await this.changeLabelContent(this.room_list,type);
          break;
        case "TEMPERATURE":
          await this.changeColor(this.room_list, manager, type);
          await this.changeLabelContent(this.room_list,type);

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
      this.inSimulation = true;
      window.addEventListener("beforeunload", this.beforeUnloadListener, {capture: true});
      axios.put(`/api/bimiot/start/${this.store.currentProjectName}`, {})
    },
    pause: function () {
      window.removeEventListener("beforeunload", this.beforeUnloadListener, {capture: true});
      axios.put(`/api/bimiot/pause/${this.store.currentProjectName}`, {});
    },
    stop: function () {
      this.inSimulation = false;
      this.playing = false;
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

  async mounted() {
    document.getElementById("model").style.filter = "blur(2px)";
    document.getElementById("progress-bar").style.visibility = "visible";
    this.moveComponentToSubDiv()
    const container = document.getElementById('model');
    const viewer = new IfcViewerAPI({container});
    this.viewer = viewer;
    viewer.axes.setAxes();
    viewer.grid.setGrid();
    await viewer.IFC.setWasmPath('../../IFCjs/');

    await viewer.IFC.loader.ifcManager.parser.setupOptionalCategories({
      [IFCSPACE]: true,
      [IFCOPENINGELEMENT]: false
    });

    await this.loadFile();
    console.log("finished load file");
    await new Promise((resolve, reject) => {
      this.createAllSubsets(this.room_list);
      resolve();
    });

    const input = document.getElementById("file-input");

    input.addEventListener("change",

        async (changed) => {
          const file = changed.target.files[0];
          const ifcURL = URL.createObjectURL(file);
          const model = await viewer.IFC.loadIfcUrl(ifcURL, true);
          this.model = model;


          model.removeFromParent();

          document.getElementById("model").style.filter = "blur(2px)";
          document.getElementById("progress-bar").style.visibility = "visible";


          const structure = await this.showStructure(viewer, model.modelID);
          this.structure = structure;


          const types = await viewer.IFC.getAllItemsOfType(model.modelID, IFCSENSORTYPE, true);
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


          window.onmousemove = () => {
            if (this.viewer === null) {
              return;
            }
            this.viewer.IFC.selector.prePickIfcItem()
          };
          window.ondblclick = async () => {
            const {modelID, id} = await viewer.IFC.selector.pickIfcItem(true);
            const type = viewer.IFC.loader.ifcManager.getIfcType(modelID, id);
            if (type === "IFCSPACE" || type === "IFCDISTRIBUTIONCONTROLELEMENT") {
              this.entityData = (type === "IFCSPACE" ? "Pièce" : "Capteur") + " - " + id;
            } else {
              viewer.IFC.selector.unpickIfcItems(); // Unselect everything that is not room or sensor
            }
          }

          let floors = await viewer.IFC.loader.ifcManager.createSubset(floor);
          let sensors = await viewer.IFC.loader.ifcManager.createSubset(sensor);
          let walls = await viewer.IFC.loader.ifcManager.createSubset(wall);
          //  let sp = await viewer.IFC.loader.ifcManager.createSubset(spaces);


          const scene = this.viewer.context.getScene();
          scene.add(floors);
          scene.add(sensors);
          scene.add(walls);


          await this.getSensors(structure, manager, model.modelID);
          this.sendMapping();
          const response = {sensorType: "HUMIDITY", roomIfcID: "207", color: "#0A0046", sensorIfcID: "sensor1"};
          this.subscribe(response);
          await new Promise(r => this.createAllSubsets(this.room_list, manager));


          // console.log(this.viewer.context.getScene().children, "kids")
          //console.log(this.roomIdToMesh["207"])

          // let room = manager.getSubset(model.modelID,this.roomIdToMesh["207"],"207");
          //console.log("im messh",room)
          /* let meshCount = (scene.children.filter(obj => obj.material && obj.material.type !== undefined && obj.material.type === "MeshLambertMaterial").length)-1
           console.log(meshCount, "im count")
           const room_ids_iter = Object.keys(this.room_list);
           this.knowledge = (meshCount*100)/room_ids_iter.length;
           console.log( this.knowledge,"hello ")*/
          //room.material.color.set(0x00ff00);
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
  filter: blur(0px);
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
  left: 0;
  bottom: 0;
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



#progress-bar  {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  visibility: hidden;
  color: #0A0046;
  font-size: 20px;
}

#playBtn{
  bottom: 150px;
  left: 35px;
  color: white;
  background-color: #0A0046;
}

#stopBtn{
  bottom: 170px;
  left: 35px;
  color: white;
  background-color: #0A0046;
}

.container {
  margin-top: 80px;
}
</style>
