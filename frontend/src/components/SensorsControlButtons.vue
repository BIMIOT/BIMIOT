<template>
  <v-container fluid id="sensorsControl">
    <v-row class="justify-end">
      <v-btn class="mx-3 my-3">Température</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn class="mx-3 my-3">Humidité</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn class="mx-3 my-3">Co2</v-btn>
    </v-row>
    <v-row class="justify-end">
      <v-btn class="mx-3 my-3">Luminosité</v-btn>
    </v-row>
  </v-container>
</template>

<script>


export default {
  name: "SensorsControlButtons"
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
