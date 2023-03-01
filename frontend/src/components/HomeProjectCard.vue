<template>
  <v-card
      class="text-center elevation-12 mb-5 rounded-xl"
      max-width="400"
      v-if="show"
      style="background: transparent;"
  >
    <v-card-title style="color: #0A0046">
      {{ title }}
    </v-card-title>
    <v-card-actions class="justify-center">
      <v-btn
          variant="elevated"
          class="text-white rounded-xl"
          style="background-color: #023D57;"
          @click="sendProjectName"
          icon="mdi-location-enter"
      />
      <v-btn
          variant="outlined"
          icon="mdi-delete"
          color="error"
          @click="this.dialog = true"
      ></v-btn>
    </v-card-actions>
  </v-card>

  <div class="text-center">
    <v-dialog
        v-model="dialog"
        width="auto"
    >
      <v-card>
        <v-card-text>
          Êtes-vous sûr de vouloir supprimer ce projet ?
        </v-card-text>
        <v-card-actions>
          <v-btn color="red" @click="deleteProject">Confirmer</v-btn>
          <v-btn color="blue" @click="dialog = false">Annuler</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>

</template>

<script>
import axios from "axios";
import {projectStore} from "@/store/project";

export default {
  props: ["title"],
  data() {
    return {
      show: true,
      dialog: false,
    }
  },
  setup() {
    const store = projectStore();
    return {store};
  },
  methods: {
    sendProjectName() {
      this.store.currentProjectName = this.title;
      this.$router.push({name: "simulation"});
    },

    async deleteProject() {
      this.show = false;
      this.dialog = false;
      const response = await axios
          .delete("/api/bimiot/projects/" + this.title, {})
          .catch((error) => {
            console.log("Error of deleting project: ", error);
          })
      console.log("Response of deleting project: ", response);
      //refresh the parent page
      this.$emit("delete")
    }
  }

}
</script>