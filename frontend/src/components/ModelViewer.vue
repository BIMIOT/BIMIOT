<template>
  <section>
    <div>
      <input type="file" id="file-input" />
      <v-btn id="play" v-on:click="start()" >Play</v-btn>
      <v-btn id="stop" v-on:click="stop()">Stop</v-btn>
      <ColorPickerSensor v-on:meshcolor="updateMeshes" id="colorPickers"/>
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
import { IFCSPACE,IFCSLAB,IFCOPENINGELEMENT, IFCDISTRIBUTIONCONTROLELEMENT, IFCWALLSTANDARDCASE, IFCSENSORTYPE, IFCSENSOR } from 'web-ifc';
import SensorsList from './SensorsList.vue'

import * as THREE from 'three';

import SensorsControlButtons from "@/components/SensorsControlButtons";
import ColorPickers from "@/components/ColorPickers";
import ColorPickerSensor from "@/components/ColorPickerSensor";


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
            currentSenseType:"temp",
            structure: undefined,
            sensor_types: {},
            room_by_color: {},
            colors: {
                  temperature: [
                          { id: 0, value: '#ff0000', intList: [1,3] },
                          { id: 1, value: '#00ff00', intList: [4, 10] },
                          { id: 2, value: '#0000ff', intList: [11, 20] },
                          { id: 3, value: '#ffff00', intList: [21, 50] }
                        ],
                  humidity: [
                    { id: 0, value: '#f62727', intList: [1,3] },
                    { id: 1, value: '#00ff00', intList: [4, 10] },
                    { id: 2, value: '#ff5900', intList: [11, 20] },
                    { id: 3, value: '#ffff00', intList: [21, 50] }
                  ],
                  luminosity: [
                    { id: 0, value: '#ff0000', intList: [1,3] },
                    { id: 1, value: '#00ff00', intList: [4, 10] },
                    { id: 2, value: '#0000ff', intList: [11, 20] },
                    { id: 3, value: '#ffff00', intList: [21, 50] }
                  ],
                  co2: [
                    { id: 0, value: '#ff0000', intList: [1,3] },
                    { id: 1, value: '#00ff00', intList: [4, 10] },
                    { id: 2, value: '#0000ff', intList: [11, 20] },
                    { id: 3, value: '#ffff00', intList: [21, 50] }
                  ]
            },
            room_list: {1:{"TEMPERATURESENSOR":[{IFCid:1,DataId:1,value:0}]}}, // roomId:{type:[IFCid:"val", DataId:"val", value:"val"]}
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
          tempMeshes:[
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x668cff,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0xffff99,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0xffcc33,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0xee6600,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x990000,
              depthTest: false,
            })
          ],
          humMeshes:[
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x05192C,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0xD0AE8B,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0xE8E4E2,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x73CCD8,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x52B1D2,
              depthTest: false,
            })
          ],
          co2Meshes:[
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x24a6f2,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0xe4c844,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x10394c,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x94a651,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0xb7b7b7,
              depthTest: false,
            })
          ],
          lumMeshes:[
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0x000000,
              depthTest: false,
            }),
            new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: 0xFFFF00,
              depthTest: false,
            })
          ],
          currentColorRange: []
        }
    },
    methods: {
      subscribe: function(greeting) {

        const response = greeting;
        if (this.model === undefined) {
          return;
        }
        console.log(response["roomIfcID"])
        console.log(response["sensorIfcID"] in this.room_list)
        if(!(response["roomIfcID"]  in this.room_list)) {
           console.log(" i didn go ")
          return;
        }

        let mesh  = null
        for (let i = 0; i < this.colors.temperature.length; i++) {
          if (response["value"] <= this.colors.temperature[i].intList[this.colors.temperature[i].intList.length-1]) {
             console.log(response["value"])
            mesh = new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: new THREE.Color(this.colors.temperature[i].value).getHex(),
              depthTest: false,
            })
            break;
          }
        }

        for (let i = 0; i < this.colors.humidity.length; i++) {
          if (response["value"] <= this.colors.humidity[i].intList[this.colors.humidity[i].intList.length-1]) {
            console.log(response["value"])
            mesh = new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: new THREE.Color(this.colors.humidity[i].value).getHex(),
              depthTest: false,
            })
            break;
          }
        }

        for (let i = 0; i < this.colors.co2.length; i++) {
          if (response["value"] <= this.colors.co2[i].intList[this.colors.co2[i].intList.length-1]) {
            console.log(response["value"])
            mesh = new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: new THREE.Color(this.colors.co2[i].value).getHex(),
              depthTest: false,
            })
            break;
          }
        }

        for (let i = 0; i < this.colors.luminosity.length; i++) {
          if (response["value"] <= this.colors.luminosity[i].intList[this.colors.luminosity[i].intList.length-1]) {
            console.log(response["value"])
            mesh = new MeshLambertMaterial({
              transparent: true,
              opacity: 0.3,
              color: new THREE.Color(this.colors.luminosity[i].value).getHex(),
              depthTest: false,
            })
            break;
          }
        }

        const manager = this.viewer.IFC.loader.ifcManager;
        if(this.room_by_color[response["roomIfcID"]] !== undefined) {
          manager.removeSubset(this.model.modelID, this.room_by_color[response["roomIfcID"]].color,"new");
        }

        this.room_by_color[response["roomIfcID"]] = { sensorType: response["sensorType"], color: mesh };

       if(response["sensorType"] === this.currentSenseType) {

         manager.createSubset({
           modelID: this.model.modelID,
           ids: [response["roomIfcID"]],
           material: this.room_by_color[response["roomIfcID"]].color,
           scene: this.viewer.context.getScene(),
           removePrevious: false,
           customID: "new"
         });
         console.log(" wow im here")
       }
      },
     convertHexToInt: function(colors) {
        return colors.map(color => {
          var color2 = new THREE.Color(color.value);
           console.log(color2.getHex())
          return new MeshLambertMaterial({
            transparent: true,
            opacity: 0.3,
            color: color2.getHex(),
            depthTest: false,
          });
        });
      },
    updateMeshes: function (data) {
      this.tempMeshes = this.convertHexToInt(data.temperature)
      console.log(this.tempMeshes)
    },
    updateParent: function (type) {
        this.currentSenseType = type
        switch (type) {
          case 'hum':
            this.currentColorRange = this.humMeshes;
            break;
          case 'lum':
            this.currentColorRange = this.lumMeshes;
            break;
          case 'co2':
            this.currentColorRange = this.co2Meshes;
            break;
          default:
            this.currentColorRange = this.tempMeshes;
        }
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

                this.room_list[relIDs.expressID] = {};
                console.log(relIDs.expressID);
            }
            for (let component in relIDs.children) {
                if (relIDs.type === "IFCSPACE" && relIDs.children[component].type === "IFCDISTRIBUTIONCONTROLELEMENT") {
                    const sensor = await manager.getItemProperties(modelID, relIDs.children[component].expressID);
                    console.log(sensor);
                    const type_name = this.sensor_types[sensor.ObjectType.value];
                    if (this.room_list[relIDs.expressID][type_name] == undefined) {
                      this.room_list[relIDs.expressID][type_name] = [];
                    }
                    this.room_list[relIDs.expressID][type_name].push({IFCid:relIDs.children[component].expressID,DataId:sensor.ObjectType.value,value:0});
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
     viewer.IFC.loader.ifcManager.parser.setupOptionalCategories({
        [IFCSPACE]: true,
        [IFCOPENINGELEMENT]: false
      });





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
            //console.log(await viewer.IFC.getProperties(model.modelID, 283, true));

            
            //const spaces = await viewer.IFC.getAllItemsOfType(model.modelID, IFCSPACE, true);

            const types = await viewer.IFC.getAllItemsOfType(model.modelID, IFCSENSORTYPE, true);
            for (let type in types) {
              this.sensor_types[types[type].Name.value] = types[type].PredefinedType.value;
            }

            const manager = this.viewer.IFC.loader.ifcManager;
            await this.getSensors(structure, manager, model.modelID);

            /**
             * HERE IS THE CODE YOU WANT IT START FROM HERE 
             * */

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


            const spaces = {
              modelID: model.modelID,
              ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID,IFCSPACE,false),
              removePrevious: true,
              material: this.invisibleMat,
              customID:"stuff4"
            }

            var floors = await viewer.IFC.loader.ifcManager.createSubset(floor);
            var sensors = await viewer.IFC.loader.ifcManager.createSubset(sensor);
            var walls = await viewer.IFC.loader.ifcManager.createSubset(wall)
            var sp = await viewer.IFC.loader.ifcManager.createSubset(spaces);


            console.log(this.room_list);
            const scene = this.viewer.context.getScene();
            scene.add(floors);
            scene.add(sensors);
            scene.add(walls);


            this.currentColorRange = this.tempMeshes;

            setInterval(async () => {

              let min = 1;
              let max = 50;
              let randomNumber = Math.floor(Math.random() * (max - min + 1)) + min;
              let greeting = {
                "roomIfcID": 207,
                "sensorIFCid": 688,
                "value": randomNumber,
                "sensorType": "hum"
              }
              this.subscribe(greeting)


            }, 3000);



            /*
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
#colorPickers {
  position: absolute !important;
  z-index: 100 !important;
}
.v-application__wrap {
  min-height: auto;
}
</style>
