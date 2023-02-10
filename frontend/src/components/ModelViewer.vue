<template>
  <section>
    <div>
      <input type="file" id="file-input"/>
      <v-btn id="play" v-on:click="start()">Play</v-btn>
      <v-btn id="stop" v-on:click="stop()">Stop</v-btn>
      <ColorPickerSensor v-on:meshcolor="updateMeshes" id="colorPickers"/>
      <SensorsList :room_list="room_list"/>
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
      currentSenseType: "temp",
      structure: undefined,
      sensor_types: {},
      room_list: {1: {"TEMPERATURESENSOR": [{IFCid: 1, DataId: 1, value: 0}]}}, // roomId:{type:[IFCid:"val", DataId:"val", value:"val"]}
      sensorMapping: [{"roomId":1, "sensorMappingDTO":[{"IFCSensorId":1,"DataSetSensorId":1}]}], // roomId:[{IFCsensorId:"1",DatasetId:"1"}]invisibleMat: new MeshLambertMaterial({
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
      tempMeshes: [
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
      humMeshes: [
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
      co2Meshes: [
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
      lumMeshes: [
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
    async loadFile(viewer) {
      //  TODO : replace 'Brice' by project name
      const response = await axios.get('api/bimiot/project/files/Brice', {
        responseType: 'blob',
      });
      console.log(response);
      const ifcURL = URL.createObjectURL(response);
      this.model = await viewer.IFC.loadIfcUrl(ifcURL);
      console.log(this.model);
    },
    subscribe: function (greeting) {
      const response = greeting;
      if (this.model === undefined) {
        return;
      }
      console.log(response["roomIfcID"])
      console.log(response["sensorIfcID"] in this.room_list)
      if (!(response["roomIfcID"] in this.room_list) || !(response["sensorIfcID"] in this.room_list)) {
        console.log(" i didn go ")
        return;
      }
      const manager = this.viewer.IFC.loader.ifcManager;
      manager.removeSubset(this.model.modelID, this.tempMeshes[0], "new");

      manager.createSubset({
        modelID: this.model.modelID,
        ids: [response["roomIfcID"]],
        material: this.tempMeshes[1],
        scene: this.viewer.context.getScene(),
        removePrevious: false,
        customID: "new"
      });
      console.log(" wow im here")
    },
    convertHexToInt: function (colors) {
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
    newSubsetOfType: async function (viewer, category) {
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
    showStructure: async function (viewer, modelID) {
      const manager = viewer.IFC.loader.ifcManager;
      const relIDs = await manager.getSpatialStructure(modelID);
      console.log(relIDs);
      return relIDs;
    },
    getSensors: async function (relIDs, manager, modelID) {
      if (relIDs.type === "IFCSPACE") {
this.room_list[relIDs.expressID] = {};
        this.sensorMapping.push({"roomId":relIDs.expressID, "sensorMappingDTO":[]});
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
          this.room_list[relIDs.expressID][type_name].push({
            IFCid: relIDs.children[component].expressID,
            DataId: sensor.ObjectType.value,
            value: 0
          });this.sensorMapping[this.sensorMapping.length-1].sensorMappingDTO.push({"IFCSensorId":relIDs.children[component].expressID,"DataSetSensorId":sensor.ObjectType.value.split(":")[1]});
        }
        await this.getSensors(relIDs.children[component], manager, modelID);
      }
    },
    start: function () {
      axios
          .put('/api/bimiot/start/' + 'etienne', {}) // TODO : replace "etienne" with project name
          .then((data) => {
                console.log('Success:', data);
              })
              .catch((error) => {
                console.error('Error:', error);
              });
    },
    stop: function () {
      axios.put('/api/bimiot/stop/' + 'etienne', {}) // TODO : replace "etienne" with project name
              .then((data) => {
                console.log('Success:', data);
              })
              .catch((error) => {
                console.error('Error:', error);
              });
        },
          sendMapping: function (){
          axios.post("/api/bimiot/mapping", this.sensorMapping)
            // fetch("/api/bimiot/mapping",{
            //   method: 'POST',
            //   headers:{'Content-Type': 'application/json'},
            //   body: JSON.stringify(this.sensorMapping)
            // })
          .then((data) => {
                  console.log('Success:', data);
                })
                .catch((error) => {
                  console.error('Error:', error);
                });
    }
  },
  created: function () {
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
    const viewer = new IfcViewerAPI({container});
    this.viewer = viewer;
    viewer.axes.setAxes();
    viewer.grid.setGrid();
    viewer.IFC.setWasmPath('../../IFCjs/');
    // const ifcapi = new IfcAPI();
    viewer.IFC.loader.ifcManager.parser.setupOptionalCategories({
      [IFCSPACE]: true,
      [IFCOPENINGELEMENT]: false
    });

    this.loadFile();
    const input = document.getElementById("file-input");

    input.addEventListener("change",

        async (changed) => {
          //await ifcapi.Init();
          const file = changed.target.files[0];
          const ifcURL = URL.createObjectURL(file);
          const model = await viewer.IFC.loadIfcUrl(ifcURL);
          this.model = model;
          console.log(this.model);
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
            console.log(JSON.stringify(this.sensorMapping));
            this.sendMapping();

          /**
           * HERE IS THE CODE YOU WANT IT START FROM HERE
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


          console.log(this.room_list);

          /*window.onclick = async () => {
            //const {modelID, id} = await viewer.IFC.selector.pickIfcItem(true);
           // const props = await viewer.IFC.getProperties(model.modelID, id, true, false);
            //await viewer.IFC.selector.highlightIfcItem(false)
            //console.log(props);
          }*/

          /* function get_random (list) {
             return list[Math.floor((Math.random()*list.length))];
           }*/


          const scene = this.viewer.context.getScene();
          scene.add(floors);
          scene.add(sensors);
          scene.add(walls);

          let array = this.co2Meshes;
          let index = 0;
          let m = this.invisibleMat;


          let s = this.viewer.context.getScene();

          this.currentColorRange = this.tempMeshes;
          const greeting = {
            "roomIfcID": 207,
            "sensorIFCid": 688
          }
          this.subscribe(greeting)


          /* setInterval(async () => {

             let space = manager.createSubset({
               modelID: model.modelID,
               ids: await viewer.IFC.loader.ifcManager.getAllItemsOfType(model.modelID,IFCSPACE,false),
               material: this.currentColorRange.at(index),
               removePrevious: true,
               customID: "new"
             });

             scene.add(space)

             index = (index + 1) % this.currentColorRange.length;


             manager.removeSubset(model.modelID,this.currentColorRange.at(index) , "new");

           }, 5000);*/


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

#play {
  position: relative;
  color: blue;
  margin: 0.5em 0.5em 0.5em;
  z-index: 10;
}

#stop {
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
