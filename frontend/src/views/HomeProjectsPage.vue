<template>
  <v-container fluid>
    <v-row align="center">
      <v-col cols="12" class="text-center">
        <img :src="logoPage" width="90" height="90">
        <h1 style="font-family: Helvetica; font-size: 30px; color: #0A0046">BIMIOT</h1>
      </v-col>
    </v-row>
    <div>

      <div v-if="this.projects.length > 8">
        <v-pagination v-model="page" :length="Math.round(this.projects.length/this.pageSize)" @next="page = $event" @prev="page = $event"></v-pagination>
      </div>
      <v-row dense align="center">
        <v-col cols="12">
          <v-row>
            <v-col
                v-for="(project, index) in projects.slice(startVisible, endVisible)"
                :key="index"
                cols="12"
                md="6"
                lg="3"
            >
              <HomeProjectCard :project="project" @delete="getAllProjects"/>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </div>
    <v-row class="my-3">
      <v-col cols="12">
        <v-btn
            fab
            dark
            prepend-icon="mdi-plus-thick"
            color="#0A0046"
            class="text-white rounded-xl"
            @click="toCreateProjectPage">
          Ajouter un projet
        </v-btn>

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
      logoPage: require("@/assets/bimiotlogo.png"),
      projects: [],
      page: 1,
      startVisible: 0,
      endVisible: 8,
      length: 0,
      pageSize: 8
    };
  },
  methods: {
    toCreateProjectPage() {
      this.$router.push({name: 'create-project'});
    },
    getAllProjects() {
      fetch("api/bimiot/projects")
          .then(response => response.json())
          .then(data => {
            this.projects = data;
          })
    }
  },
  watch: {
    page() {
      this.startVisible = (this.page - 1) * this.pageSize;
      this.endVisible = this.startVisible + this.pageSize;
    }
  },
  mounted() {
   this.getAllProjects();
  }
}
</script>

<style>

</style>
