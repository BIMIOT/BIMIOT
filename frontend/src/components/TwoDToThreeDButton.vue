<template>
  <v-btn class="Two2d3D" icon id="not"
         color='#0A0046'
         dark
         style="color: white"
  >
   {{state}}
  </v-btn>
</template>
<script>

export default {
  name: 'TwoDToThreeDButton',
  props: {
    state: {}
  },
}
</script>
<style>
.Two2d3D{
  bottom: 80px;
  left: 35px;
  color: blue;
}
</style>
