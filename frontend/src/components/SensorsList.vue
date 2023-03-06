<script>

import TreeViewSensors from "@/components/TreeViewSensors";

export default {
  components: {
    TreeViewSensors,
  },
  props: {
    room_list: {
      type: Object,
      required: true
    }
  },
  methods: {
    search(obj) {
      this.$refs.treeViewSensor.searchFrom3d(obj);
    },
    emitId(id) {
      this.$emit("id-emit",id)
    }
  },
  data () {
    return {
      roomId: "",
      dialog: false
    }
  },
}
</script>
<template>
  <div>
    <v-btn icon class="mx-3 my-3"
           color='#0A0046'
           dark
           @click.stop=" dialog = true"
    >
      <v-icon color="white">
        mdi-access-point
      </v-icon>
    </v-btn>

    <v-card>

      <v-layout>
        <v-navigation-drawer
            v-bind:width="425"
            v-model="dialog"
            temporary
        >
          <v-list-item
              title="Liste des capteurs"
              nav
          >
            <template v-slot:append>
              <v-btn
                  variant="text"
                  icon="mdi-chevron-left"
                  size="large"
                  @click.stop="dialog = !dialog"
              ></v-btn>
            </template>
          </v-list-item>
          <div v-if="dialog">
            <TreeViewSensors v-on:id-emit="emitId" ref="treeViewSensor" :room_list="this.room_list"/>
          </div>
        </v-navigation-drawer>
      </v-layout>
    </v-card>
  </div>
</template>
<style>

.input-wrapper {
  font-size: 16px;
}

.v-list-item--nav .v-list-item-title {
  font-size: 1.1rem;
}
</style>
