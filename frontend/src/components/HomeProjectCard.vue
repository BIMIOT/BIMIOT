<template>
  <v-card class="text-center mx-auto" v-if="show">
    <v-card-title>
      {{ title }}
    </v-card-title>
    <v-card-actions>
      <v-btn
        color="green"
        @click="sendProjectName"
      >
        Details
      </v-btn>
      <v-btn
          variant="text"
          color="red"
          @click="deleteProject"
      >
        Supprime
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import axios from "axios";

export default {
  props: ["title"],
  data(){
    return {
      show: true
    }
  },
  methods: {
    sendProjectName() {
      this.$router.push({name: "simulation", params: {project: this.title}});
    },

    async deleteProject() {
      this.show = false;

      const response = await axios
          .delete("/api/bimiot/projects/" + this.title, {})
          .catch((error) =>{
            console.log("Error of deleting project: ",error);
          })
      console.log("Response of deleting project: ",response);
    }
  }

}
</script>

<style scoped>

</style>
