<template>
  <v-row justify="center">
    <v-btn
        icon
        class="hideValue"
        color='#0A0046'
    >
      <v-icon color="white">
        {{ hide ? 'mdi-eye-off-outline' : 'mdi-eye-outline'}}
      </v-icon>
    </v-btn>
  </v-row>

</template>

<script>
export default {
  props:["hide"],
  name: "HideValueButton"
}
</script>

<style scoped>


.hideValue{
  color: blue;
}

</style>