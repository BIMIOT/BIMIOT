<script>

export default {
  components: {
    //ModelViewer
  },
  created() {
    document.title = 'BimIot'
  }
}
</script>

<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style>
  body::-webkit-scrollbar{
    display: none;
  }
  body{
    height: 100vh;
    background: linear-gradient(to bottom, #ffffff, #D1FFFF);
  }
</style>
