<template>
  <v-container fill-height fill-width>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-form @submit.prevent="saveDatas">
          <v-card class="text-center mx-auto">
            <v-card-title>
              Créer un projet
            </v-card-title>
            <v-card-text>
              <v-text-field
                  v-model="projectName"
                  label="Nom du projet"
                  @change="validateForm"
                  required
              ></v-text-field>
              <v-file-input
                  v-model="ifc"
                  label="Fichier IFC"
                  accept=".ifc"
                  @change="validateForm"
                  required
                  @click:clear="this.valid = false"
              ></v-file-input>
              <v-file-input
                  v-model="dataset"
                  label="Dataset"
                  accept="application/json"
                  @change="validateForm"
                  required
                  @click:clear="this.valid = false"
              ></v-file-input>
            </v-card-text>

            <v-card-actions class="justify-center">
              <v-btn
                  variant="elevated"
                  :loading="loading"
                  :disabled="!this.valid || loading"
                  color="success"
                  dark
                  type="submit"
              > Créer
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-form>
        <div>
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
      host:null,
      port:null,
      ifc: null,
      dataset: null,
      snackbar: false,
      timeout: 5000,
      errorMessage: null,
      valid: false,
      loading: false
    }
  },
  methods: {
    validateForm() {
      this.valid = !!this.projectName && !!this.ifc && !!this.dataset;
    },
    async saveDatas() {
      this.loading = true;
      const formData = new FormData();
      formData.append('name', this.projectName);
      formData.append('ifc', this.ifc[0]);
      formData.append('dataset', this.dataset[0]);

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
            this.loading = false;
          });

    }
  },
  mounted() {

    this.validateForm();
  }
}
</script>
