<template>
  <v-row justify="center">
    <v-btn class="Two2d3D" icon id="Two2d3D"
           color='#0A0046'
           dark
           style="color: white"
    >
     {{state}}
    </v-btn>
  </v-row>
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
.Two2d3D {
  color: blue;
}

</style>
