<template>
  <div>
    <div class="sensor-item" v-show="this.selectedType === 'TEMPERATURE'">
      <!--      <font-awesome-icon :icon="['fas', 'temperature-half']" class="my-3"/>-->
      <div class="sensor-icon">
        <v-icon color="#0A0046" size="28">mdi-home-thermometer</v-icon>
      </div>
      <color-pickers v-on:colorToValue="updateTempColorToValue" v-on:click="this.showModal=true"/>
    </div>
    <div class="sensor-item" v-show="this.selectedType === 'HUMIDITY'">
      <!--      <font-awesome-icon :icon="['fas', 'tint']" class="my-3"/>-->
      <div class="sensor-icon">
        <v-icon color="#0A0046" size="28">mdi-water-percent</v-icon>
      </div>
      <color-pickers v-on:colorToValue="updateHumColorToValue" v-on:click="this.showModal=true"/>
    </div>
    <div class="sensor-item" v-show="this.selectedType === 'LIGHT'">
      <!--      <font-awesome-icon :icon="['fas', 'lightbulb']" class="my-3"/>-->
      <div class="sensor-icon">
        <v-icon color="#0A0046" size="28">mdi-home-lightbulb</v-icon>
      </div>
      <color-pickers v-on:colorToValue="updateLumColorToValue" v-on:click="this.showModal=true"/>
    </div>
    <div class="sensor-item" v-show="this.selectedType === 'CO2'">
      <!--      <font-awesome-icon :icon="['fas', 'leaf']" class="my-3"/>-->
      <div class="sensor-icon">
        <v-icon color="#0A0046" size="28">mdi-molecule-co2</v-icon>
      </div>
      <color-pickers v-on:colorToValue="updateCo2ColorToValue" v-on:click="this.showModal=true"/>
    </div>

    <div class="save-buttons" v-if="showModal">
      <v-btn color="primary" @click="saveData">Save</v-btn>
      <v-btn @click="showModal = false">Cancel</v-btn>
    </div>

  </div>
</template>

<script>

import ColorPickers from "@/components/ColorPickers";
import axios from 'axios';
import {sensorsStore} from "@/store/sensors";
import {projectStore} from "@/store/project";

export default {
  props: ['selectedType'],
  name: "ColorPickerSensor",
  components: {
    ColorPickers
  },
  data() {
    return {
      showModal: false,
      temperatureColorToValue: [],
      humidityColorToValue: [],
      luminosityColorToValue: [],
      co2ColorToValue: [],
    }
  },
  setup() {
    const projectStore1 = projectStore();
    const sensorsStore1 = sensorsStore();
    return {projectStore1, sensorsStore1};
  },
  methods: {
    updateTempColorToValue(colorToValue) {
      this.temperatureColorToValue = colorToValue;
    },
    updateHumColorToValue(colorToValue) {
      this.humidityColorToValue = colorToValue;
    },
    updateCo2ColorToValue(colorToValue) {
      this.luminosityColorToValue = colorToValue;
    },
    updateLumColorToValue(colorToValue) {
      this.co2ColorToValue = colorToValue;
    },
    updateColorToValue(colorToValue){
      this.temperatureColorToValue = colorToValue;
      this.humidityColorToValue = colorToValue;
      this.luminosityColorToValue = colorToValue;
      this.co2ColorToValue = colorToValue;
    },
    saveData() {
      this.showModal = false;

      console.log(JSON.stringify(this.temperatureColorToValue));
      console.log(JSON.stringify(this.humidityColorToValue));
      console.log(JSON.stringify(this.co2ColorToValue));
      console.log(JSON.stringify(this.luminosityColorToValue));

      let config = {
        headers: {
          'Content-Type': 'application/json',
        }
      }

      let sensorsColors = {"sensorColorApis": {}};
      sensorsColors["sensorColorApis"]["TEMPERATURE"] = this.temperatureColorToValue;
      sensorsColors["sensorColorApis"]["LIGHT"] = this.luminosityColorToValue;
      sensorsColors["sensorColorApis"]["HUMIDITY"] = this.humidityColorToValue;
      sensorsColors["sensorColorApis"]["CO2"] = this.co2ColorToValue;


      console.log(" sensorColors: ", JSON.stringify(sensorsColors));

      // console.log(JSON.stringify(sensorsColors));
      // axios.put(`/api/bimiot/projects/colors/${this.projectStore1.currentProjectName}`, JSON.stringify(sensorsColors), config)
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
.color-bar {
  align-content: center;
}

.sensor-item {
  background-color: white;
  display: flex;
  flex-direction: column;
  padding: 8px;
}

.sensor-item i {
  font-size: 36px;
  margin-bottom: 12px;
}

.save-buttons {
  display: flex;
  background-color: white;
  padding: 8px;
  justify-content: space-between;
}

.sensor-icon {
  display: flex;
  justify-content: center;
}
</style>
