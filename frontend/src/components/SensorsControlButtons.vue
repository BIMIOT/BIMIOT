<template>
  <v-container fluid id="sensorsControl">
    <v-row class="justify-end">
      <v-btn  v-on:click="childMethod('TEMPERATURE')" class="mx-3 my-3" :value="'TEMPERATURE'" :class="{ 'active': selected === 'TEMPERATURE' }">Température</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn v-on:click="childMethod('HUMIDITY')" class="mx-3 my-3" :value="'HUMIDITY'" :class="{ 'active': selected === 'HUMIDITY' }">Humidité</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn v-on:click="childMethod('CO2')" class="mx-3 my-3" :value="'CO2'" :class="{ 'active': selected === 'CO2' }">Co2</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn class="mx-3 my-3" v-on:click="childMethod('LIGHT')" :value="'LIGHT'" :class="{ 'active': selected === 'LIGHT' }">Luminosité</v-btn>
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
  z-index: 10;
}

.active {
  background-color: blue;
  color: white;
}
</style>
