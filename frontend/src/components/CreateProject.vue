<template>
  <v-btn id="show-modal" @click="showModal = true">Create new project</v-btn>
  <modal v-if="showModal" @close="showModal = false">
    <v-form>
      <h1>filename</h1>
    </v-form>
    <v-btn id="save-file" type="submit" @click=handleSave()>Save</v-btn>
  </modal>
</template>

<script>
export default {
  name: "CreateProject",
  data() {
    return {
      showModal: false,
      info:null
    }
  },
  methods:{
    async handleSave() {
      const API_URL = '/api/bimiot/project?name={test}';
      const directory = "testDirectory"

      try {
        const response = await fetch(`${API_URL}`, {
          method: 'POST',
          body: JSON.stringify(directory)
        });
        const result = await response.json();

        if (result !== null) {
          console.log("Création du client réussi")
          //this.$router.push("dashboard");
          this.$emit("close");
        }
      } catch (error) {
        console.log(error.message())
      }

    }
  }
}
</script>

<style scoped>

</style>