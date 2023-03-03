<template>
  <div id="search-bar">
    <v-row align="center" fill-height>
      <v-col cols="8">
        <v-text-field
            variant="solo"
            label="Search templates"
            single-line
            hide-details
            v-model="searchTerm"
            @input="searchGlobal"

        ></v-text-field>
      </v-col>
      <v-col cols="4">
        <v-select
            :items="options"
            hide-details
            v-model="selectedOption"
            align-self-center
        ></v-select>
      </v-col>
    </v-row>
  </div>




  <v-divider></v-divider>
  <v-card
      class="mx-auto"
      width="100%"
  >
    <div v-if="!sensor">
      <v-list
          v-for="(i) in localRoomList"
          :key="i"
      >
        <v-list-group value="Admin">
          <template v-slot:activator="{ props }">
            <v-list-item
                v-bind="props"
                :title="i.roomId"
            ></v-list-item>
          </template>

          <v-list
              v-for="(y) in i.sensors"
              :key="y"
          >
            <v-list-group value="Admin">
              <template v-slot:activator="{ props }">
                <v-list-item
                    v-bind="props"
                    :title="y.type + ' / '+ y.DataId"
                ></v-list-item>
              </template>
              <v-list-item
                  :title="y.DataId"
              ></v-list-item>
              <v-list-item
                  :title="y.IFCid"
              ></v-list-item>
              <v-list-item
                  :title="!y.value? 'Vide' : y.value"
              ></v-list-item>
              <v-list-item
                  :title="y.roomId"
              ></v-list-item>
            </v-list-group>
          </v-list>
        </v-list-group>

      </v-list>
    </div>
    <div v-if="sensor">
      <v-list
              v-for="(i) in localSensorList"
              :key="i"
      >
            <v-list-group value="Admin">
              <template v-slot:activator="{ props }">
                <v-list-item
                    v-bind="props"
                    :title="i.type + ' / '+ i.DataId"
                ></v-list-item>
              </template>
              <v-list-item
                  :title="i.DataId"
              ></v-list-item>
              <v-list-item
                  :title="i.IFCid"
              ></v-list-item>
              <v-list-item
                  :title="!i.value? 'Vide' : i.value"
              ></v-list-item>
              <v-list-item
                  :title="i.roomId"
              ></v-list-item>
            </v-list-group>

      </v-list>
    </div>
  </v-card>
</template>

<script>
import treeview from "vue3-treeview";
import "vue3-treeview/dist/style.css";

export default {
  name: "TreeViewSensors",
  props: {
    searchFrom3dModel: {},
    room_list: {},
  },
  data() {
    return {
      options:["rooms","sensors"],
      selectedOption: "rooms",
      sensor: false,
      localRoomList: [],
      localSensorList: [],
      searchTerm: "",
      oldSensorList: [],
      oldRoomList: [],
    };
  },
  watch: {
    selectedOption: {
      handler(val, oldVal) {
        this.sensor = val !== "rooms";
      }
  }
  },
  mounted() {
    this.localRoomList = this.generateRoomList(this.room_list);
    this.localSensorList = this.generateSensorList();
    console.log(this.localSensorList)
  },
  methods: {
    findSensorByDataId(dataId) {
      return this.localSensorList.filter(sensor => {
        return sensor.DataId.includes(dataId);
      })
    },
    findRoomByRoomId(roomId) {
      return this.localRoomList.filter(room => {
        return room.roomId.includes(roomId);
      });
    },
    searchFrom3d(roomId) {
       this.searchTerm = roomId;
       this.searchGlobal()
    },
    searchGlobal() {
      this.localRoomList = this.oldRoomList;
      this.localSensorList = this.oldSensorList;
      this.sensor = this.selectedOption !== "rooms";

      if(this.selectedOption === "rooms") {
        let rooms = this.findRoomByRoomId(this.searchTerm);
        if(rooms.length > 0 && this.searchTerm) {
          this.localRoomList = rooms;
          this.sensor = false;
        }
      } else  {
        let sensor =  this.findSensorByDataId(this.searchTerm);
        if (sensor.length > 0 && this.searchTerm) {
          this.sensor = true;
          this.localSensorList = sensor;
        }
      }
    },
    generateSensorList() {
       let output = this.localSensorList = this.localRoomList.flatMap((room) => {
            return room.sensors.map((sensor) => {
              return {
                type: sensor.type,
                IFCid: sensor.IFCid,
                DataId: sensor.DataId,
                roomId: room.roomId
              };
            });
          });
         this.oldSensorList = output;
         return output;
        },
    generateRoomList(input) {
      const output = [];

      for (const roomId in input) {
        const room = {
          roomId,
          sensors: []
        };

        for (const sensorType in input[roomId]) {
          const sensorData = input[roomId][sensorType];

          for (const sensor of sensorData) {
            const sensorObj = {
              type: sensorType,
              ...sensor
            };

            room.sensors.push(sensorObj);
          }
        }

        output.push(room);
      }
      this.oldRoomList = output;
      return output;
    },
    },
};
</script>

<style>


#search-bar {
  margin: 5px;
}


</style>
