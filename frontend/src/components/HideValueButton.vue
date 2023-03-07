<template>
    <v-btn icon class="mx-3 my-3" color='#0A0046'>
      <v-icon color="white">
        {{ hide ? 'mdi-eye-off-outline' : 'mdi-eye-outline'}}
      </v-icon>
    </v-btn>
</template>

<script>
export default {
  props:["hide"],
  name: "HideValueButton"
}
</script>