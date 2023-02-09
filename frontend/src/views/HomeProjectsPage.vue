<template>
  <v-container fluid>
    <v-row dense>
      <v-btn
        fab
        dark
        color="indigo"
        @click="toCreateProjectPage">
        <v-icon dark>
          mdi-plus
        </v-icon>
      </v-btn>
      <v-col
          v-for="name in names"
          :key="name"
          cols="3"
      >
        <HomeProjectCard :title="name"/>
      </v-col>
    </v-row>
  </v-container>
  <div>
    <HomeProjectCard :title="message"/>
  </div>
</template>

<script>
import HomeProjectCard from "@/components/HomeProjectCard";

export default {
  components: {HomeProjectCard},
  data() {
    return {
      names: []
    };
  },
  methods :{
    toCreateProjectPage(){
      this.$router.push("/create-project");
    }
  },
  mounted() {
    fetch("api/bimiot/projects")
        .then(response => response.json())
        .then(data => {
          this.names = data;
        })
  }
}
</script>

<style scoped>

</style>