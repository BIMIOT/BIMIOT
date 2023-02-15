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
              <font-awesome-icon :icon="['fas', 'thermometer-half']"  class="my-3"/>
              <color-pickers v-on:colors="updateTemp" />
            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'tint']" class="my-3" />
              <color-pickers v-on:colors="updateHum" v-model="humidityColors"  />
            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'lightbulb']" class="my-3"/>
              <color-pickers v-on:colors="updateLight"  v-model="luminosityColors" />
            </div>
            <div class="sensor-item">
              <font-awesome-icon :icon="['fas', 'leaf']" class="my-3" />
              <color-pickers  v-on:colors="updateCo2"  v-model="co2Colors" />
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
      co2Colors: []
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
    saveData() {
      console.log("colors: ", this.temperatureColors);
      const data = {
        temperature: this.temperatureColors,
        humidity: this.humidityColors,
        luminosity: this.luminosityColors,
        co2: this.co2Colors
      }
      this.$emit("meshcolor", data)
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
