<script>

export default {
  components: {
    //ModelViewer
  },
  created() {
    document.title = 'Vue IFC.js Example'
  }
}
</script>

<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style>

html {
  overflow-y: hidden !important;
}

#app {
  box-sizing: border-box;
  width: 500px;
  margin: 0 auto;
  padding: 0 20px 20px;
}

button,
label,
input {
  display: inline-flex;
  font-family: "Open sans", sans-serif;
  font-size: 100%;
  line-height: 1.15;
  margin: 0;
}

button,
input {
  overflow: visible;
}

button {
  text-transform: none;
}

button,
[type="button"],
[type="reset"],
[type="submit"]{
  -webkit-appearance: none;
}

</style>