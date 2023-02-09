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
        Simulation
      </v-btn>
        <v-btn
            variant="text"
            color="red"
            @click="this.dialog = true"
        >
          Supprime
        </v-btn>
    </v-card-actions>
  </v-card>

  <div class="text-center">
  <v-dialog
      v-model="dialog"
      width="auto"
  >
    <v-card>
      <v-card-text>
        Êtes-vous sûr de vouloir supprimer cet projet ?
      </v-card-text>
      <v-card-actions>
        <v-btn color="success"  @click="dialog = false">Annuler</v-btn>
        <v-btn color="primary"  @click="deleteProject">Confirmer</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  </div>

</template>

<script>
import axios from "axios";

export default {
  props: ["title"],
  data(){
    return {
      show: true,
      dialog:false,
    }
  },
  methods: {
    sendProjectName() {
      this.$router.push({name: "simulation", params: {project: this.title}});
    },

    async deleteProject() {
      this.show = false;
      this.dialog =false;
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
