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
          location="center"
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
      fetch('/api/bimiot/projects/folder', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          projectName: this.projectName,
        }),
      })
          .then((response) => response.json())
          .then((data) => {
            if (data.code === '400') {
              throw new Error(data.message);
            }
            const name = data.projectName;
            const formData = new FormData();
            formData.append('files', this.ifc);
            formData.append('files', this.dataset);
            fetch(`/api/bimiot/projects/files/${name}`, {
              method: 'POST',
              body: formData
            })
                .then(response => response.json())
                .then(data => {
                  this.$router.push({name: 'home'});
                })
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
