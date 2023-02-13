<template>
  <v-card class="text-center text-black" color="grey" v-on:click="sendProjectName">
    {{ title }}
  </v-card>
</template>

<script>
export default {
  props: ["title"],
  methods: {
    sendProjectName() {
      this.$router.push({name: "simulation", params: {project: this.title}});
    }
  }
}
</script>

<style scoped>

</style>