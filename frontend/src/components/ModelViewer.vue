<template>
  <section>
    <div>
      <input type="file" id="file-input" />
      <v-btn id="play" v-on:click="start()" >Play</v-btn>
      <v-btn id="stop" v-on:click="stop()">Stop</v-btn>
      <SensorsList :room_list="room_list"/>
      <SensorsControlButtons v-on:child-method="updateParent"/>
    </div>

    <p id="properties-text">
      ID:
      {{ entityData }}
    </p>
    <div id="model" />
  </section>
</template>

<script>
import { IfcViewerAPI } from 'web-ifc-viewer';
import { MeshLambertMaterial } from 'three';
import axios from 'axios';
import sockjs from "sockjs-client/dist/sockjs"
import * as StompJs from '@stomp/stompjs';
import { IFCSPACE,IFCSLAB,IFCOPENINGELEMENT, IFCDISTRIBUTIONCONTROLELEMENT, IFCWALLSTANDARDCASE } from 'web-ifc';
import {IfcAPI} from "three/examples/jsm/loaders/ifc/web-ifc-api";
import * as THREE from "three";
import SensorsList from './SensorsList.vue'
import SensorsControlButtons from "@/components/SensorsControlButtons";
export default {
    name: 'ModelViewer',
    props: ['token', 'projectId', 'discipline'],
    components: {
      SensorsList,
      SensorsControlButtons
    },
    data() {
        return {
            entityData: '',
            client: undefined,
            viewer: undefined,
            model: undefined,
            structure: undefined,
            room_list: [{roomId:1, sensors:[{sensorIFCid:1,sensorDataSetId:1}]}],
          invisibleMat: new MeshLambertMaterial({
            transparent: true,
            opacity: 0.4,
            color: 0xff0000,
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

        }
    },
    methods: {
      updateParent: function (data) {
        console.log(data)
      },
      newSubsetOfType: async function (viewer,category) {
        const manager = viewer.IFC.loader.ifcManager;
        const ids = await manager.getAllItemsOfType(0, category, false);
        console.log(ids);

        return manager.createSubset({
          modelID: 0,
          scene: this.viewer.context.getScene(),
          ids: [722],
          applyBVH: true,
          removePrevious: true,
          customID: category.toString(),
        });
      },
        showStructure: async function(viewer, modelID) {
            const manager = viewer.IFC.loader.ifcManager;
            const relIDs = await manager.getSpatialStructure(modelID);
            console.log(relIDs);
            return relIDs;
        },
        getSensors: async function(relIDs, manager, modelID) {
            if (relIDs.type === "IFCSPACE") {
                const sensorList = [];
                this.room_list.push({roomId:relIDs.expressID, sensors:sensorList});
                console.log(relIDs.expressID);
            }
            for (let component in relIDs.children) {
                if (relIDs.type === "IFCSPACE" && relIDs.children[component].type === "IFCDISTRIBUTIONCONTROLELEMENT") {
                    const sensor = await manager.getItemProperties(modelID, relIDs.children[component].expressID);
                    this.room_list[this.room_list.length-1].sensors.push({sensorIFCid:relIDs.children[component].expressID, sensorDataSetId:sensor.Name.value});
                }
               await this.getSensors(relIDs.children[component], manager, modelID);
            }
        },
        start: function() {
          axios
              .post('http://localhost:8082/api/start')
              .then(response => (console.log(response)));
        },
        stop: function() {
          axios
              .post('http://localhost:8082/api/stop')
              .then(response => (console.log(response)));
        }
    },
    created: function() {
        console.log("Starting connection to WebSocket Server");
        let client = new StompJs.Client({
          brokerURL: 'ws://localhost:8082/sensors-data-endpoint',
          debug: function (str) {
            //console.log(str);
          },
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
            return new sockjs('http://localhost:8082/sensors-data-endpoint');
          };
        }

        client.onConnect = (frame) => {
          // Do something, all subscribes must be done is this callback
          // This is needed because this will be executed after a (re)connect
          client.subscribe('/data/sensors', (greeting) => {
            const response = JSON.parse(greeting.body);
            if (this.model === undefined) {
              return;
            }
            console.log(response["sensorIfcID"]);
            if (response["sensorIfcID"] === 283) {
              if (response["value"] === 20) {
                changeColor(this.structure, 201, response["sensorIfcID"], this.preSelectMat, this.preSelectMatBlue, 1);
              } else {
                changeColor(this.structure, 201, response["sensorIfcID"], this.preSelectMatBlue, this.preSelectMat, 1);
              }
            }
            if (response["sensorIfcID"] === 722) {
              if (response["value"] === 20) {
                changeColor(this.structure, 234, response["sensorIfcID"], this.preSelectMat, this.preSelectMatBlue, 2);
              } else {
                changeColor(this.structure, 234, response["sensorIfcID"], this.preSelectMatBlue, this.preSelectMat, 2);
              }
            }

         });

          console.log("Successfully subscribed to the backend server...");
        };

        client.onStompError = function (frame) {
          // Will be invoked in case of error encountered at Broker
          // Bad login/passcode typically will cause an error
          // Complaint brokers will set `message` header with a brief message. Body may contain details.
          // Compliant brokers will terminate the connection after any error
          console.log('Broker reported error: ' + frame.headers['message']);
          console.log('Additional details: ' + frame.body);
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
/*        for (let component in relIDs.children) {
          if (relIDs.expressID === roomId && relIDs.children[component].type === "IFCSPACE") {
            const manager = this.viewer.IFC.loader.ifcManager;
            manager.removeSubset(this.model.modelID, previousMaterial, groupId);
            manager.createSubset({
              modelID: this.model.modelID,
              ids: [relIDs.children[component].expressID],
              material: material,
              scene: this.viewer.context.getScene(),
              removePrevious: false,
              customID: groupId
            });
          }
          changeColor(relIDs.children[component], roomId, sensorId, material, previousMaterial);
        }*/
      }
    },
    mounted() {
      const container = document.getElementById('model');
      const viewer = new IfcViewerAPI({ container });
      this.viewer = viewer;
      viewer.axes.setAxes();
      viewer.grid.setGrid();
      viewer.IFC.setWasmPath('../../IFCjs/');
     // const ifcapi = new IfcAPI();
     /*viewer.IFC.loader.ifcManager.parser.setupOptionalCategories({
        [IFCSPACE]: true
        ,
        [IFCOPENINGELEMENT]: false
      });*/

      const input = document.getElementById("file-input");

      input.addEventListener("change",

          async (changed) => {
            //await ifcapi.Init();
            const file = changed.target.files[0];
            const ifcURL = URL.createObjectURL(file);
            const model = await viewer.IFC.loadIfcUrl(ifcURL);
            
            /*
            this.model = model;
            model.removeFromParent();
            */
            const structure = await this.showStructure(viewer, model.modelID);
            this.structure = structure;
            //console.log(await viewer.IFC.getProperties(model.modelID, 283, true));

            
            const spaces = await viewer.IFC.getAllItemsOfType(model.modelID, IFCSPACE, true);
            const manager = this.viewer.IFC.loader.ifcManager;
            await this.getSensors(structure, manager, model.modelID);

            /**
             * HERE IS THE CODE YOU WANT IT START FROM HERE 
             * */
             /*
            const floor = {
              modelID: model.modelID,
              ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID,IFCSLAB,false),
              removePrevious: true,
              customID:"stuff"
            }

            const sensor = {
              modelID: model.modelID,
              ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID,IFCDISTRIBUTIONCONTROLELEMENT,false),
              material: this.sensorColor,
              removePrevious: true,
              customID:"stuff2"
            }

            const wall = {
              modelID: model.modelID,
              ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID,IFCWALLSTANDARDCASE,false),
              removePrevious: true,
              customID:"stuff3"
            }

            const space = {
              modelID: model.modelID,
              ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID,IFCSPACE,false),
              removePrevious: true,
              material: this.invisibleMat,
              customID:"stuff4"
            }

            var floors = await viewer.IFC.loader.ifcManager.createSubset(floor);
            var sensors = await viewer.IFC.loader.ifcManager.createSubset(sensor);
            var walls = await viewer.IFC.loader.ifcManager.createSubset(wall)
            var sp = await viewer.IFC.loader.ifcManager.createSubset(space);


            const manager = this.viewer.IFC.loader.ifcManager;
            for (const space in spaces) {

              console.log(spaces[space])

            }

            let json = {rooms:[]};
            await this.getSensors(structure, json.rooms, manager, model.modelID);

            console.log(JSON.stringify(json));

            window.onclick = async () => {
              //const {modelID, id} = await viewer.IFC.selector.pickIfcItem(true);
             // const props = await viewer.IFC.getProperties(model.modelID, id, true, false);
              //await viewer.IFC.selector.highlightIfcItem(false)
              //console.log(props);
            }

            function get_random (list) {
              return list[Math.floor((Math.random()*list.length))];
            }


            const scene = this.viewer.context.getScene();
            scene.add(floors);
            scene.add(sensors);
            scene.add(walls);
            scene.add(sp);

            const strcture = await viewer.IFC.getSpatialStructure(model.modelID);
            console.log("hello ",strcture);

            axios
                .post('http://localhost:8082/api/rooms', json)
                .then(response => (console.log(response)));
                */

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
  z-index: 10;
}

#play{
  position: relative;
  color: blue;
  margin: 0.5em 0.5em 0.5em;
  z-index: 10;
}

#stop{
  position: relative;
  color: blue;
  margin: 0.5em 0.5em 0.5em;
  z-index: 10;
}

#properties-text {
    position: absolute;
    left: 0%;
    bottom: 0%;
    z-index: 100;
}
</style>
