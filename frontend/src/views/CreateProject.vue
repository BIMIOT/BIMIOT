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
      <h3>Dataset</h3>
      <pre>{{ dataset }}</pre>
    </div>
    <div>
      <h3>IFC File</h3>
      <pre>{{ ifc }}</pre>
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
            const name = data.projectName;
            const formData = new FormData();
            formData.append('files', this.ifc);
            formData.append('files', this.dataset);
            fetch(`/api/bimiot/projects/files/${name}`, {
              method: 'POST',
              body: formData
            })
            this.$router.push("/");
          })
          .then((response) => response.json())
          .then((data) => {
            console.log(data);
          })

    }
  }
}
</script>

<style scoped>

</style>
