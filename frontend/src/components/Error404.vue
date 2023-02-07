<template>
  <div id="main">
    <div class="fof">
      <v-row  align="center" justify="center">
        <h1>Error 404</h1>
      </v-row>
      <v-row justify="center">
        <v-btn
            class="ma-2"
            color="primary"
            @click="() => {this.$router.push('/')}"
            max-width="404"
        >
          Go back
        </v-btn>
      </v-row>


    </div>

  </div>
</template>

<script>
export default {
  name: "Error404Page"
}
</script>

<style scoped>
*{
  transition: all 0.6s;
}

html {
  height: 100%;
}

body{
  font-family: 'Lato', sans-serif;
  color: #888;
  margin: 0;
}

#main{
  display: table;
  width: 100%;
  height: 100vh;
  text-align: center;
}

.fof{
  display: table-cell;
  vertical-align: middle;
}

.fof h1{
  font-size: 50px;
  display: inline-block;
  padding-right: 12px;
  animation: type .5s alternate infinite;
}

@keyframes type{
  from{box-shadow: inset -3px 0px 0px #888;}
  to{box-shadow: inset -3px 0px 0px transparent;}
}
</style>