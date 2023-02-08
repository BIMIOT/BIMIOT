<template>
  <div>
    <h3>Create/Update Project Form</h3>
    <form novalidate>
      <div>
        <ColorPickers/>
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
            @change="handleIFCFile">
      </div>
      <div>
        <input type="submit" @click="saveDatas" value="Save">
      </div>
    </form>
    <div>
      <pre>{{ifc}}</pre>
    </div>
  </div>
</template>
<script>

import ColorPickers from "@/components/ColorPickers";

export default {
  components: {ColorPickers},

  data() {
    return {
      projectName: null,
      ifc: null
    }
  },
  methods: {
    handleIFCFile() {
      let file = this.$refs.ifcFile.files[0];
      this.createBase64File(file);
    },
    createBase64File(fileObject){
      const reader = new FileReader();
      reader.onload = (e) => {
        this.ifc = e.target.result;
      }
      reader.readAsDataURL(fileObject);
    },
    async saveDatas() {
      fetch('/api/bimiot/project', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          directoryName: this.projectName,
          ifcFile: this.ifc,
          datasetFile: "nothing"
        }),
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
