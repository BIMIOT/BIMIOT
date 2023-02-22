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
              <color-pickers v-on:colorToValue="updateTempColorToValue"/>
            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'tint']" class="my-3"/>
              <color-pickers v-on:colorToValue="updateHumColorToValue"/>
            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'lightbulb']" class="my-3"/>
              <color-pickers v-on:colorToValue="updateLumColorToValue"/>
            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'leaf']" class="my-3"/>
              <color-pickers v-on:colorToValue="updateCo2ColorToValue"/>
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
import {projectStore} from "@/store/project";

export default {
  name: "ColorPickerSensor",
  components: {
    ColorPickers
  },
  setup() {
    const store = projectStore();
    return {store};
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
    saveData() {
      let sensorsColors = {"sensorColorApis": {}};
      sensorsColors["sensorColorApis"]["TEMPERATURE"] = this.temperatureColorToValue;
      sensorsColors["sensorColorApis"]["LIGHT"] = this.luminosityColorToValue;
      sensorsColors["sensorColorApis"]["HUMIDITY"] = this.humidityColorToValue;
      sensorsColors["sensorColorApis"]["CO2"] = this.co2ColorToValue;

      this.store.updateProjectColors(sensorsColors);

      this.showModal = false

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
