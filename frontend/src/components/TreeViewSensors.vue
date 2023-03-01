<template>
  <div id="sidebar-left" class="sticky top-0 h-full w-full bg-gray-100 p-4">
    <tree :nodes="nodes" :config="config"></tree>
  </div>
</template>

<script>
import treeview from "vue3-treeview";
import "vue3-treeview/dist/style.css";

export default {
  name: "TreeViewSensors",
  props: {
    room_list: {  },
  },
  components: {
    tree: treeview,
  },
  data() {
    return {
      config: {
        roots: Object.keys(this.room_list),
      },
      nodes: {},
    };
  },
  mounted() {
    this.nodes = this.generateNodes(this.room_list);
  },
  methods: {
    generateNodes(roomList) {
      const nodes = {};
      for (const roomId in roomList) {
        const roomNode = {
          text: `Pièce: ${roomId}`,
          children: [],
        };
        for (const type2 in roomList[roomId]) {
          if (type2 !== "type") {
            for (const sensor of roomList[roomId][type2]) {
              if (sensor.IFCid) {
                const sensorNode = {
                  text: " Type " + type2 +  " / Data set Id : " + sensor.DataId + " / IFC ID :" + sensor.IFCid + " / " +  (sensor.value === undefined ? "Aucune" : sensor.value)
                };
                roomNode.children.push(sensor.IFCid)
                nodes[sensor.IFCid] = sensorNode;
              }
            }
          }
        }
        nodes[roomId] = roomNode;
      }
      console.log(nodes)
      return nodes;
    },
  },
};
</script>

<style>

</style>
