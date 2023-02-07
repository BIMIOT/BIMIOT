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
        <input
            id="ifcFile"
            type="file"
            ref="ifcFile"
            @change="saveIFCFile">
      </div>
      <div>
        <input type="submit" @click="handleSave" value="Save">
      </div>
    </form>
    <div v-if="formData != null">
      <pre>{{formData}}</pre>
    </div>
  </div>
</template>
<script>

export default {
  data() {
    return {
      projectName: null,
      formData: null
    }
  },
  methods: {
    saveIFCFile() {
      let file = this.$refs.ifcFile.files[0];
      this.formData = new FormData();
      this.formData.append("file", file);
    },
    async handleSave() {
      fetch('/api/bimiot/fileUpload', {
        method: 'POST', // or 'PUT'
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        body: this.formData,
      })
          .then((response) => response.json())
          .then((data) => {
            console.log('Success:', data);
          })
          .catch((error) => {
            console.error('Error:', error);
          });
    }
  }
}
</script>

<style scoped>

</style>