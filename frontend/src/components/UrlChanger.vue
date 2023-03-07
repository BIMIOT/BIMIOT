<template>
  <v-container>
    <v-row align="center" justify="center">
      <v-col
          cols="12"
          sm="6"
          md="4"
      >
        <v-text-field   variant="solo" v-model="host" :rules="[() => host.startsWith('https://') || host.startsWith('http://') || 'This field is need to be a valid address']"  label="Simulator Link" prepend-icon="mdi-access-point-network"></v-text-field>
      </v-col>

      <v-col
          cols="12"
          sm="6"
          md="4"
      >
        <v-text-field
            v-model="port"
            label="Port"
            type="number"
            variant="solo"
            number
            append-icon="mdi-content-save"
            @click:append="updateSimulatorLink()"
        ></v-text-field>
      </v-col>


    </v-row>
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
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  name: "UrlChanger",
  data() {
    return {
      host:null,
      port:null,
      snackbar: false,
      timeout: 5000,
      errorMessage: null,
    }
  },
  methods : {
    async getDefaultLink() {
      fetch('/api/bimiot/simulations/settings', {
        method: 'GET',
      })
          .then((response) => response.json())
          .then((data) => {
            if (data.code === '400') {
              throw new Error(data.message);
            }
            const apiUrl = data.url;
            const apiUrlParts = apiUrl.split(":");
            const hostname = apiUrlParts[0] + ":" + apiUrlParts[1];
            const port = apiUrlParts[2];
            this.host = hostname;
            this.port = port;
            console.log(this.host, " ", this.port)
          })
          .catch(error => {
            this.errorMessage = error.message;
            this.snackbar = true;
            this.loading = false;
          });
    },
    async updateSimulatorLink() {
      let newUrlBody = {host: "http://188.166.194.147", port: parseInt("80", 10)}
      await axios.put(`/api/bimiot/simulations`, JSON.stringify(newUrlBody))
    }
  },
  mounted() {
    this.getDefaultLink()
  }
}
</script>

<style scoped>

</style>
