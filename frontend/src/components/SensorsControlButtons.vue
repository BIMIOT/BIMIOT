<template>
  <v-container fluid id="sensorsControl">
  <div class="fill-content">
      <v-btn
          icon
          v-on:click="childMethod('TEMPERATURE')"
          class="mx-3 my-3"
          :value="'TEMPERATURE'" :class="{ 'active': selected === 'TEMPERATURE' }"
          :color="pickColor('TEMPERATURE')"
      >
        <v-icon :color="selected === 'TEMPERATURE'? '#0A0046' : '#ffffff'">
          mdi-thermometer
        </v-icon>
      </v-btn>
  </div>
    <div class="fill-content">
      <v-btn
          icon
          v-on:click="childMethod('HUMIDITY')"
          class="mx-3 my-3"
          :value="'HUMIDITY'"
          :class="{ 'active': selected === 'HUMIDITY' }"
          :color="pickColor('HUMIDITY')"
      >
        <v-icon
            :color="selected === 'HUMIDITY'? '#0A0046' : '#ffffff'">
          mdi-water-percent
        </v-icon>
      </v-btn>
    </div>
    <div class="fill-content">
      <v-btn
          icon
          v-on:click="childMethod('CO2')"
          class="mx-3 my-3"
          :value="'CO2'" :class="{ 'active': selected === 'CO2' }"
          :color="pickColor('CO2')"
      >
        <v-icon :color="selected === 'CO2'? '#0A0046' : '#ffffff'">
          mdi-molecule-co2
        </v-icon>
      </v-btn>
    </div>
    <div class="fill-content">
      <v-btn
          icon
          v-on:click="childMethod('LIGHT')"
          class="mx-3 my-3"
          :value="'LIGHT'" :class="{ 'active': selected === 'LIGHT' }"
          :color="pickColor('LIGHT')"
      >
        <v-icon :color="selected === 'LIGHT'? '#0A0046' : '#ffffff'">
          mdi-lightbulb-on
        </v-icon>
      </v-btn>
    </div>
  </v-container>
</template>

<script>


export default {
  data() {
    return {
      iconColor: "white",
      selected: "TEMPERATURE",
    }
  },
  name: "SensorsControlButtons",
  methods: {
    childMethod: function(type) {
      this.$emit('child-method', type)
      this.selected = type;
    },
    pickColor: function(value) {
      if (this.selected === value) {
        return '#d1FFFF'; // red color for selected button
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

#sensorsControl {
  position: fixed;
  bottom: 30px;
  float: right;
  right: 25px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  width: max-content;
  background-color: #023D57;
  border-radius: 25px 25px 25px 25px;
  padding: 0;
}


.fill-content{
  float: left;
}


div {
  display : flex;
}

.active {
  background-color: blue;
  color: white;
}
</style>
