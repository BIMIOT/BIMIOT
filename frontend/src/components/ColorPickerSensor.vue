<template>
  <div>
    <v-btn @click="showModal = true">Open Modal</v-btn>
    <v-dialog v-model="showModal">
      <v-card>
        <v-card-title>
          <span>Select Colors for Sensors</span>
        </v-card-title>
        <v-card-text>
          <div class="sensor-container">
            <div class="sensor-item">
              <v-icon icon="mdi-vuetify"></v-icon>
              <color-pickers v-on:colors="updateTemp" v-on:values="updateTempValues"/>

            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'tint']" class="my-3" />
              <color-pickers v-on:colors="updateHum" v-on:values="updateHumValues" v-model="humidityColors"  />
            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'lightbulb']" class="my-3"/>
              <color-pickers v-on:colors="updateLight" v-on:values="updateLightValues" v-model="luminosityColors" />
            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'leaf']" class="my-3" />
              <color-pickers  v-on:colors="updateCo2" v-on:values="updateCo2Values" v-model="co2Colors" />
            </div>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="saveData">Save</v-btn>
          <v-btn color="secondary" @click="showModal = false">Cancel</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

import ColorPickers from "@/components/ColorPickers";
import axios from 'axios';

export default {
  name: "ColorPickerSensor",
  components: {
    ColorPickers
  },
  data() {
    return {
      showModal: false,
      temperatureColors: [],
      humidityColors: [],
      luminosityColors: [],
      co2Colors: [],
      temperatureValues: [],
      humidityValues: [],
      luminosityValues: [],
      co2Values: []
    }
  },
  methods: {
    updateTemp(colors) {
      this.temperatureColors = colors;
    },
    updateHum(colors) {
      this.humidityColors = colors;
    },
    updateCo2(colors) {
      this.co2Colors = colors;
    },
    updateLight(colors) {
      this.luminosityColors = colors;
    },
    updateTempValues(values) {
      this.temperatureValues= values;
    },
    updateHumValues(values) {
      this.humidityValues = values;
    },
    updateCo2Values(values) {
      this.co2Values = values;
    },
    updateLightValues(values) {
      this.luminosityValues = values;
    },
    saveData() {
      //TODO first icon doesn't display correctly
      //console.log("colors: ", this.temperatureColors);

      console.log("ints: ",this.temperatureValues)
      let config = {
        headers: {
          'Content-Type': 'application/json',
        }
      }

      let typesColors = {"typesColor":{}};
      typesColors["typesColor"]["TEMPERATURE"] = {"colors":[],"values":this.temperatureValues};
      for (let i in this.temperatureColors) {
        typesColors["typesColor"]["TEMPERATURE"]["colors"].push(this.temperatureColors[i].value);
      }
      typesColors["typesColor"]["HUMIDITY"] = {"colors":[],"values":this.humidityValues};
      for (let i in this.humidityColors) {
        typesColors["typesColor"]["HUMIDITY"]["colors"].push(this.humidityColors[i].value);
      }
      typesColors["typesColor"]["LIGHT"] = {"colors":[],"values":this.luminosityValues};
      for (let i in this.luminosityColors) {
        typesColors["typesColor"]["LIGHT"]["colors"].push(this.luminosityColors[i].value);
      }
      typesColors["typesColor"]["CO2"] = {"colors":[],"values":this.co2Values};
      for (let i in this.co2Colors) {
        typesColors["typesColor"]["CO2"]["colors"].push(this.co2Colors[i].value);
      }

      // console.log(JSON.stringify(typesColors));
      // axios.post("/api/bimiot/colors", JSON.stringify(typesColors), config)
      //     .then((data) => {
      //       console.log('Success:', data);
      //     })
      //     .catch((error) => {
      //       console.error('Error:', error);
      //     });
      //
      // this.showModal = false
    }
  }
}
</script>

<style>
.sensor-container {
  display: flex;
  justify-content: space-between;
}

.sensor-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.sensor-item i {
  font-size: 36px;
  margin-bottom: 12px;
}
</style>
