<template>
  <v-container fluid>
    <v-row dense align="center">
      <v-btn
          fab
          dark
          prepend-icon="mdi-plus"
          color="indigo"
          @click="toCreateProjectPage">
        Ajouter un projet
      </v-btn>
      <v-col
          v-for="name in names"
          :key="name"
          cols="3"
      >
        <HomeProjectCard :title="name" @delete="getAllProjects"/>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import HomeProjectCard from "@/components/HomeProjectCard";
export default {
  components: {HomeProjectCard},
  data() {
    return {
      names: ['Project1','Project1','Project1','Project1','Project1','Project1','Project1','Project1'],
    };
  },
  methods: {
    toCreateProjectPage() {
      this.$router.push({name: 'create-project'});
    },
    getAllProjects(){
      fetch("api/bimiot/projects")
          .then(response => response.json())
          .then(data => {
            this.names = data;
          })
    }
  },
  mounted() {
    this.getAllProjects()
  }
}
</script>
