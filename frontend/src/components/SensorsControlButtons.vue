<template>
  <v-container fluid id="sensorsControl">
    <v-row class="justify-end">
      <v-btn
          icon
          v-on:click="childMethod('TEMPERATURE')"
          class="mx-3 my-3"
          :value="'TEMPERATURE'" :class="{ 'active': selected === 'TEMPERATURE' }"
          :color="pickColor('TEMPERATURE')"
      >
        <v-icon color="white">
          mdi-home-thermometer
        </v-icon>
      </v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn
          icon
          v-on:click="childMethod('HUMIDITY')"
          class="mx-3 my-3"
          :value="'HUMIDITY'"
          :class="{ 'active': selected === 'HUMIDITY' }"
          :color="pickColor('HUMIDITY')"
      >
        <v-icon
            color="white">
          mdi-water-percent
        </v-icon>
      </v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn
          icon
          v-on:click="childMethod('CO2')"
          class="mx-3 my-3"
          :value="'CO2'" :class="{ 'active': selected === 'CO2' }"
          :color="pickColor('CO2')"
      >
        <v-icon color="white">
          mdi-molecule-co2
        </v-icon>
      </v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn
          icon
          v-on:click="childMethod('LIGHT')"
          class="mx-3 my-3"
          :value="'LIGHT'" :class="{ 'active': selected === 'LIGHT' }"
          :color="pickColor('LIGHT')"
      >
        <v-icon color="white">
          mdi-home-lightbulb
        </v-icon>
      </v-btn>
    </v-row>
  </v-container>
</template>

<script>


export default {
  data() {
    return {
      selected: "TEMPERATURE",
    }
  },
  name: "SensorsControlButtons",
  methods: {
    childMethod: function(type) {
      this.$emit('child-method', type)
      this.selected = type;
      console.log(this.selected);
    },
    pickColor: function(value) {
      if (this.selected === value) {
        return '#a3a3a2'; // red color for selected button
      } else {
        return '#0A0046'; // default color for unselected button
      }
    }


  }
}
</script>

<style scoped>
.justify-end {
  display: flex;
  justify-content: flex-end;
}

#sensorsControl{
  position: relative;
  color: blue;
}

.active {
  background-color: blue;
  color: white;
}
</style>
