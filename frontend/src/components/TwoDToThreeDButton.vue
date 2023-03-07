<template>
    <v-btn class="mx-3 my-3" icon
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
