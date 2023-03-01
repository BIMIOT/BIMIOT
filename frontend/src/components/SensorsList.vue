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
      <v-btn icon id="showSensors"
        color='#0A0046'
        dark
        @click.stop="dialog = true"
      >
        <v-icon color="white">
          mdi-access-point
        </v-icon>
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
            <ul  v-for="(type,roomId) in room_list " :key="type.id">
              <li v-if='roomId!=="roomId"'>
                Pièce : {{ roomId }}
                <ul v-for="(sensor,type2) in room_list[roomId]" :key="sensor.id">
                  <li v-if='type2!=="type"'>
                    Type : {{ type2 }}
                    <ul v-for="sensor in room_list[roomId][type2]" :key="sensor.id">
                      <li v-if="sensor.IFCid">
                        Capteur : {{ sensor.IFCid }} / {{ sensor.DataId }}, valeur : {{ sensor.value !== undefined ? sensor.value : "Aucune" }}
                      </li>
                    </ul>
                  </li>
                </ul>
              </li>
              <br>
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
  bottom: 120px;
  left: 35px;
  color: blue;
}
</style>
