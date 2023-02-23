<template>
  <div   :style="{ left: colorPickerPos.x + 'px', top: colorPickerPos.y + 'px' }" @mousedown="handleMouseDown" >
    <div class="sensor-item" v-show="this.selectedType === 'TEMPERATURE'" >
      <div class="sensor-icon">
        <v-icon color="#0A0046" size="28">mdi-home-thermometer</v-icon>
      </div>
      <color-pickers v-on:save="saveData" class="color-pickers" v-on:colorToValue="updateTempColorToValue" v-on:click="this.showModal=true"/>
    </div>
    <div class="sensor-item" v-show="this.selectedType === 'HUMIDITY'">
      <div class="sensor-icon">
        <v-icon color="#0A0046" size="28">mdi-water-percent</v-icon>
      </div>
      <color-pickers  v-on:save="saveData"  class="color-pickers" v-on:colorToValue="updateHumColorToValue" v-on:click="this.showModal=true"/>
    </div>
    <div class="sensor-item" v-show="this.selectedType === 'LIGHT'">
      <div class="sensor-icon">
        <v-icon color="#0A0046" size="28">mdi-home-lightbulb</v-icon>
      </div>
      <color-pickers v-on:save="saveData"  v-on:colorToValue="updateLumColorToValue" v-on:click="this.showModal=true"/>
    </div>
    <div class="sensor-item" v-show="this.selectedType === 'CO2'">
      <div class="sensor-icon">
        <v-icon color="#0A0046" size="28">mdi-molecule-co2</v-icon>
      </div>
      <color-pickers v-on:save="saveData"  v-on:colorToValue="updateCo2ColorToValue" v-on:click="this.showModal=true"/>
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
      colorPickerPos: { x: window.innerWidth.valueOf()/2, y: window.innerHeight.valueOf()/7 },
      isDragging: false,
      lastMousePos: { x: 0, y: 0 },
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
    handleMouseDown(event) {
      // Save the initial mouse position and set the isDragging flag
      this.lastMousePos = { x: event.clientX, y: event.clientY };
      this.isDragging = true;

      // Add event listeners for mousemove and mouseup events
      document.addEventListener('mousemove', this.handleMouseMove);
      document.addEventListener('mouseup', this.handleMouseUp);
    },
    handleMouseMove(event) {
      if (!this.isDragging) return;

      // Calculate the difference between the current and last mouse positions
      const dx = event.clientX - this.lastMousePos.x;
      const dy = event.clientY - this.lastMousePos.y;

      // Update the colorPickerPos based on the difference
      this.colorPickerPos = {
        x: this.colorPickerPos.x + dx,
        y: this.colorPickerPos.y + dy,
      };

      // Save the current mouse position for the next mousemove event
      this.lastMousePos = { x: event.clientX, y: event.clientY };
    },
    handleMouseUp(event) {
      // Unset the isDragging flag and remove the event listeners
      this.isDragging = false;
      document.removeEventListener('mousemove', this.handleMouseMove);
      document.removeEventListener('mouseup', this.handleMouseUp);
    },
    updateTempColorToValue(colorToValue) {
      this.isDragging = false;
      this.temperatureColorToValue = colorToValue;
    },
    updateHumColorToValue(colorToValue) {
      this.isDragging = false;
      this.humidityColorToValue = colorToValue;
    },
    updateCo2ColorToValue(colorToValue) {
      this.isDragging = false;
      this.luminosityColorToValue = colorToValue;
    },
    updateLumColorToValue(colorToValue) {
      this.isDragging = false;
      this.co2ColorToValue = colorToValue;
    },
    updateColorToValue(colorToValue){
      this.temperatureColorToValue = colorToValue;
      this.humidityColorToValue = colorToValue;
      this.luminosityColorToValue = colorToValue;
      this.co2ColorToValue = colorToValue;
    },

    saveData(action) {

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
      console.log(" sensorColors: ",JSON.stringify(sensorsColors));

      console.log(JSON.stringify(sensorsColors));
      axios.put(`/api/bimiot/projects/colors/${this.projectStore1.currentProjectName}`, JSON.stringify(sensorsColors), config)
          .then((data) => {
            console.log('Success:', data);
          })
          .catch((error) => {
            console.error('Error:', error);
          });

    }
  }
}
</script>

<style>



/*
.color-pickers {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}*/


.sensor-item i {
  font-size: 36px;
  margin-bottom: 12px;
}

.sensor-item {

  display: inline-flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

}
/*

.sensor-icon {
  display: flex;
  justify-content: center;
}
*/



.save-buttons {
  display: flex;
  position: relative;
  width: 150%;
  z-index: 5;
}


</style>
