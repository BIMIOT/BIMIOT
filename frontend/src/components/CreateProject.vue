<template>
  <div>
    <h3>Create/Update Project Form</h3>
    <form novalidate>
      <div class="form-group">
        <input

            id="projectName"
            type="text"
            placeholder="Project Name"
            v-model="projectName">
      </div>
      <div>
        <input type="submit" @click="handleSave" value="Save">
      </div>
    </form>
  </div>
</template>
<script>

export default {
  data(){
    return {
      projectName: null
    }
  },
  methods: {
    async handleSave() {
      fetch('/api/bimiot/project', {
        method: 'POST', // or 'PUT'
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          directoryName: this.projectName
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
