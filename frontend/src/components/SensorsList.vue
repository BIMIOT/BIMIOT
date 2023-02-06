<script>
  export default {
    props: {
        room_list: {
            type: Array,
            required: true
        }
    },
    data () {
      return {
        dialog: false,
      }
    },
  }
</script>

<template>
    <v-row justify="center">
      <v-btn id="showSensors"
        color="primary"
        dark
        @click.stop="dialog = true"
      >
        Lister les capteurs
      </v-btn>
  
      <v-dialog
        v-model="dialog"
        max-width="750"
      >
        <v-card>
          <v-card-title class="text-h5">
            Liste des capteurs
          </v-card-title>
  
          <v-card-text>
            <ul>
                <li v-for="room in room_list" :key="room.roomId"> {{ room.roomId }} : {{ room.sensors }}</li>
            </ul>
          </v-card-text>
  
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="green darken-1"
              text
              @click="dialog = false"
            >
              Fermer
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-row>
  </template>

<style>
#showSensors{
  position: relative;
  color: blue;
  z-index: 10;
}
</style>