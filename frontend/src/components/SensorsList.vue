<script>
  export default {
    props: {
        room_list: {
            type: Object,
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
              <li v-for="(type,roomId) in room_list " :key="type.id">
                Pièce : {{ roomId }}
                <ul>
                  <li v-for="(sensor,type2) in room_list[roomId]" :key="sensor.id">
                    Type : {{ type2 }}
                    <ul>
                      <li v-for="sensor in room_list[roomId][type2]" :key="sensor.id">
                        Capteur : {{ sensor.IFCid }} / {{ sensor.DataId }}, valeur : {{ sensor.value }}
                      </li>
                    </ul>
                  </li>
                </ul>
              </li>
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