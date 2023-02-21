<template>
  <img :src="logo" width="60" height="57">
</template>

<script>
export default {
  name: "BimIotLogo",
  data() {
    return {
      logo: require("@/assets/bimiotlogo.png")
    }
  }
}
</script>

<style scoped>

</style>
