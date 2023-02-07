<template>
  <v-container fluid id="sensorsControl">
    <v-row class="justify-end">
      <v-btn  v-on:click="childMethod('temp')" class="mx-3 my-3" value="temp">Température</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn v-on:click="childMethod('hum')" class="mx-3 my-3">Humidité</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn v-on:click="childMethod('co2')" class="mx-3 my-3">Co2</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn class="mx-3 my-3" v-on:click="childMethod('lum')">Luminosité</v-btn>
    </v-row>
  </v-container>
</template>

<script>


export default {
  name: "SensorsControlButtons",
  methods: {
    childMethod: function(type) {
      this.$emit('child-method', type)
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
</style>
