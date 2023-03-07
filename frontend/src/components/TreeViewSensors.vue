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
          v-for="(i,index) in localRoomList "
          :key="index"
      >
        <v-list-group value="Admin">
          <template v-slot:activator="{ props }">
            <v-list-item :disabled="i.sensors.length === 0"
                v-bind="props"
                prepend-icon="mdi-home"
                :title="'Piece ' + i.roomId"
                @dblclick="emitId(i.roomId,'rooms')"
            ></v-list-item>
          </template>
          <v-list
              v-for="(y,index) in i.sensors"
              :key="index"

          >
            <v-list-group value="Admin">
              <template v-slot:activator="{ props }">
                <v-list-item
                    prepend-icon="mdi-access-point"
                    v-bind="props"
                    :title="y.type + ' / '+ y.DataId"
                >
                  <template v-slot:append>
                    <v-chip
                        class="ma-2"
                        color="#0A0046"
                        text-color="white"
                        :append-icon="typeToIcon[y.type]"
                    >
                     {{!y.value? 'Vide' : y.value + " " + unitsStore.getUnitFromType(y.type)}}
                    </v-chip>
                  </template>
                </v-list-item>

              </template>
              <v-list-item
                  prepend-icon="mdi-note-text-outline"
                  :title="'Dataset Id : ' + y.DataId"
              ></v-list-item>
              <v-list-item
                  prepend-icon="mdi-domain"
                  @dblclick="emitId(y.IFCid,'sensors')"
                  :title="'IFC id : ' + y.IFCid"
              ></v-list-item>
            </v-list-group>
          </v-list>
        </v-list-group>

      </v-list>
    </div>
    <div v-if="sensor">
      <v-list
              v-for="(i,index) in localSensorList"
              :key="index"
      >
            <v-list-group value="Admin">
              <template v-slot:activator="{ props }">
                <v-list-item
                    v-bind="props"
                    prepend-icon="mdi-access-point"
                    :title="i.type + ' / '+ i.DataId"
                >
                  <template v-slot:append>
                    <v-chip
                        class="ma-2"
                        color="#0A0046"
                        text-color="white"
                        :append-icon="typeToIcon[i.type]"
                    >
                      {{!i.value? 'Vide' : i.value + " " + unitsStore.getUnitFromType(i.type)}}
                    </v-chip>
                  </template>

                </v-list-item>
              </template>
              <v-list-item
                  prepend-icon="mdi-note-text-outline"
                  :title="'Dataset Id : ' + i.DataId"
              ></v-list-item>
              <v-list-item
                  prepend-icon="mdi-domain"
                  @dblclick="emitId(i.IFCid,'sensors')"
                  :title="'IFC id : ' + i.IFCid"
              ></v-list-item>
              <v-list-item
                  prepend-icon="mdi-home"
                  @dblclick="emitId(i.roomId,'rooms')"
                  :title="'Piece ' + i.roomId"
              ></v-list-item>
            </v-list-group>

      </v-list>
    </div>
  </v-card>
</template>

<script>
import {unitsTypeStore} from "@/store/unitsType";

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
      typeToIcon: {
        TEMPERATURE: "mdi-thermometer",
        CO2:"mdi-molecule-co",
        HUMIDITY:"mdi-water-percent",
        LIGHT:"mdi-lightbulb-on"
      },
      localSensorList: [],
      searchTerm: "",
      oldSensorList: [],
      oldRoomList: [],
    };
  },
    watch: {
      selectedOption: {
        handler(val) {
          this.sensor = val !== "rooms";
        }
      },
      room_list: {
        handler() {
          this.localRoomList = this.generateRoomList(this.room_list);
          this.localSensorList = this.generateSensorList();
        },
        deep: true
      }

    },
    setup() {
      const unitsStore = unitsTypeStore();
      return {unitsStore};
    },
    mounted() {
      this.localRoomList = this.generateRoomList(this.room_list);
      this.localSensorList = this.generateSensorList();
    },
    methods: {
      emitId(id,type) {
        this.selectedOption = type;
        this.$emit("id-emit",id)

      },
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
      searchFrom3d(obj) {
        this.searchTerm = obj.id;
        this.selectedOption = obj.type;
        this.searchGlobal()
      },
      searchGlobal() {
        this.localRoomList = this.oldRoomList;
        this.localSensorList = this.oldSensorList;
        this.sensor = this.selectedOption !== "rooms";

        if (this.selectedOption === "rooms") {
          let rooms = this.findRoomByRoomId(this.searchTerm);
          if (rooms.length > 0 && this.searchTerm) {
            this.localRoomList = rooms;
            this.sensor = false;
          }
        } else {
          let sensor = this.findSensorByDataId(this.searchTerm);
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
              roomId: room.roomId,
              value: sensor.value
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
