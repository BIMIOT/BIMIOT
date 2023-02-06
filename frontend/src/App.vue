<script>

export default {
    name: 'app',
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
    margin: 0;
    padding: 0;
    border: 0;
    height: 100%;
}

</style>