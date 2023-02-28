<template>
  <v-container fluid>
    <v-row align="center">
      <v-col cols="12" class="text-center">
        <h1 style="font-family: Arial sans-serif; font-size: 30px;">BIMIOT</h1>
      </v-col>
      <v-col cols="12" class="text-center">
        <v-btn
            fab
            dark
            prepend-icon="mdi-plus"
            color="#023D57"
            class="text-white"
            @click="toCreateProjectPage">
          Ajouter un projet
        </v-btn>
      </v-col>
    </v-row>
    <v-row dense align="center">
      <v-col cols="12">
        <v-row>
          <v-col
              v-for="name in names"
              :key="name"
              cols="12"
              md="6"
              lg="3"
          >
            <HomeProjectCard :title="name" @delete="getAllProjects"/>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import HomeProjectCard from "@/components/HomeProjectCard";
import HomePageTitle from "@/components/HomePageTitle";

export default {
  components: {HomeProjectCard, HomePageTitle},
  data() {
    return {
      names: ['Project1', 'Project1', 'Project1', 'Project1', 'Project1', 'Project1', 'Project1', 'Project1'],
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
            this.names = data;
          })
    }
  },
  mounted() {
    this.getAllProjects()
  }