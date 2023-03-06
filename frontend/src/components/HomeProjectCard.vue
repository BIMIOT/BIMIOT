<template>
  <v-card
      class="mx-auto"

  >
    <v-card-text class="py-0">
      <v-row align="center" no-gutters>
        <v-col
            class="text-h4"
            cols="6"
        >
          {{project.name}}
        </v-col>

        <v-col cols="6" class="text-right">
          <img :src="logo" width="85" height="85">
        </v-col>
      </v-row>
    </v-card-text>



    <v-divider></v-divider>

    <v-card-actions>
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
      <v-btn  size="large" @click="expand = !expand">
      Infos
      </v-btn>
    </v-card-actions>
    <v-expand-transition>
      <div v-if="expand">
        <v-list-item
            density="compact"
            :title="project.datasetFilename"
        >
          <template v-slot:prepend>
            <v-icon  style="margin: 10px" >mdi-note-text-outline</v-icon>
          </template>
        </v-list-item>

        <v-list-item
            density="compact"
            :title="project.ifcFilename"
        >
          <template v-slot:prepend>
            <v-icon  style="margin: 10px" >mdi-domain</v-icon>
          </template>
        </v-list-item>

      </div>
    </v-expand-transition>
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
  props: ["project"],
  data() {
    return {
      logo: require("@/assets/bimiot-animated-logo.gif"),
      expand: false,
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
      this.store.currentProject = this.project;
      this.$router.push({name: "simulation"});
    },

    async deleteProject() {
      this.show = false;
      this.dialog = false;
      const response = await axios
          .delete("/api/bimiot/projects/" + this.project.name, {})
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
<style>
img {
  margin-top: 15px;
}
</style>
