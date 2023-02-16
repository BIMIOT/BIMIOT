<template>
  <div>
    <h3>Create/Update Project Form</h3>
    <form novalidate>
      <div>
        <input
            id="projectName"
            type="text"
            placeholder="Project Name"
            v-model="projectName">
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

</style>
