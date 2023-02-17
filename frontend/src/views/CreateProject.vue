<template>
  <v-container fill-height fill-width>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-card class="text-center mx-auto">
          <v-card-title>
            Créer / Modifier un projet
          </v-card-title>
          <v-card-text>
            <form novalidate>
              <div>
                <input
                    id="projectName"
                    type="text"
                    placeholder="Project Name"
                    v-model="projectName"/>
              </div>
              <div>
                <label for="ifcFile">IFC File</label>
                <input
                    id="ifcFile"
                    type="file"
                    ref="ifcFile"
                    @change="handleIFCFile">
              </div>
              <div>
                <label for="datasetFile">Dataset File</label>
                <input
                    id="datasetFile"
                    type="file"
                    ref="datasetFile"
                    @change="handleDatasetFile">
              </div>
              <div>
                <v-btn
                    color="success"
                    dark
                    v-on:click="saveDatas"
                >
                  Save
                </v-btn>
              </div>
            </form>
          </v-card-text>
          <v-card-actions>
            <!-- add something here-->
          </v-card-actions>
        </v-card>
        <div class="centered-component">
          <div>
            <v-snackbar
                location="top"
                v-model="snackbar"
                :timeout="timeout"
                color="error"
            >
              {{ errorMessage }}
            </v-snackbar>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>

export default {
  components: {},
  data() {
    return {
      projectName: null,
      ifc: null,
      dataset: null,
      snackbar: false,
      timeout: 5000,
      errorMessage: null
    }
  },
  methods: {
    handleIFCFile() {
      this.ifc = this.$refs.ifcFile.files[0];
    },
    handleDatasetFile() {
      this.dataset = this.$refs.datasetFile.files[0];
    },
    async saveDatas() {
      const formData = new FormData();
      formData.append('name', this.projectName);
      formData.append('ifc', this.ifc);
      formData.append('dataset', this.dataset);
      fetch('/api/bimiot/projects', {
        method: 'POST',
        body: formData
      })
          .then((response) => response.json())
          .then((data) => {
            if (data.code === '400') {
              console.log(data);
              throw new Error(data.message);
            }
            this.$router.push({name: 'home'});
          })
          .catch(error => {
            this.errorMessage = error.message;
            this.snackbar = true;
          })

    }
  }
}
</script>

<style scoped>
.centered-component {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
</style>
