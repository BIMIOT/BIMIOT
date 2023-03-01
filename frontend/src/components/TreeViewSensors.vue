<template>
  <div id="search-bar">
    <v-text-field
        density="compact"
        variant="solo"
        label="Search templates"
        append-inner-icon="mdi-magnify"
        single-line
        hide-details
        v-model="searchTerm"
        @input="searchGlobal"
    ></v-text-field>
  </div>
  <v-divider></v-divider>
  <div id="sidebar-left" class="sticky top-0 h-full w-full bg-gray-100 p-4">
    <tree :nodes="nodes" :config="config">
    </tree>
  </div>
</template>

<script>
import treeview from "vue3-treeview";
import "vue3-treeview/dist/style.css";
import SensorSearchBar from "@/components/SensorSearchBar";

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
      searchTerm: "",
      oldNodes: {},
      configOld: {
        roots: Object.keys(this.room_list),
        openedIcon: {
          type: "shape",
          stroke: "black",
          strokeWidth: 3,
          viewBox: "0 0 24 24",
          draw: `M12 2 L2 10 V20 H22 V10 L12 2 Z`,
        },
        closedIcon: {
          type: "shape",
          stroke: "black",
          strokeWidth: 3,
          viewBox: "0 0 24 24",
          draw: `M12 2 L2 10 V20 H22 V10 L12 2 Z`,
        },
      },
      config: {
        roots: Object.keys(this.room_list),
        openedIcon: {
          type: "shape",
          stroke: "black",
          strokeWidth: 3,
          viewBox: "0 0 24 24",
          draw: `M12 2 L2 10 V20 H22 V10 L12 2 Z`,
        },
        closedIcon: {
          type: "shape",
          stroke: "black",
          strokeWidth: 3,
          viewBox: "0 0 24 24",
          draw: `M12 2 L2 10 V20 H22 V10 L12 2 Z`,
        },
      },
      nodes: {},
    };
  },
  mounted() {
    this.nodes = this.generateNodes(this.room_list);
  },
  methods: {
    searchGlobal() {
      if(!this.nodes[this.searchTerm]) {
        this.nodes = this.oldNodes
        this.config.roots = this.configOld.roots;
        return;
      }
      this.searchRooms();
      //this.searchSensors();
    },
    searchSensors () {
      const currentNode =  {}
      this.config.roots = [this.searchTerm];
      console.log(this.config.roots);
      console.log(this.nodes, "hello")
      currentNode[this.searchTerm] = this.nodes[this.searchTerm];
      for (const child of this.nodes[this.searchTerm].children) {
        currentNode[child] = (this.nodes[parseInt(child,10)])
            if(child === undefined) {
              currentNode[child] =   { text: "Valeur : Aucune"};
            } else  {
                currentNode[child] = this.nodes[child];
              }
            }

      this.nodes = currentNode;
      console.log(this.nodes, "im here bro")
    },
    searchRooms () {
      const currentNode =  {}
      currentNode[parseInt(this.searchTerm,10)] = (this.nodes[parseInt(this.searchTerm,10)])
      for (const child of this.nodes[parseInt(this.searchTerm,10)].children) {
        currentNode[child] = (this.nodes[parseInt(child,10)])
        if(this.nodes[parseInt(child,10)] !== undefined) {
          for (const childElement of this.nodes[parseInt(child,10)].children) {
            if(childElement === undefined) {
              currentNode[childElement] =   { text: "Valeur : Aucune"};
            } else  {
                currentNode[childElement] = this.nodes[childElement];

            }
          }
        }
      }

      this.nodes = currentNode;
    },
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
                  text: " Type " + type2 + " / " + roomId,
                  children: []
                };
                sensorNode.children.push(sensor.DataId+"#");
                sensorNode.children.push(sensor.IFCid);
                sensorNode.children.push(sensor.value);



                roomNode.children.push(sensor.DataId);
                nodes[sensor.DataId] = sensorNode;
                nodes[sensor.IFCid] = { text: "Ifc Id : " + sensor.IFCid };
                nodes[sensor.DataId+"#"] = { text: "Dataset Id : " + sensor.DataId };
                nodes[sensor.value] = {text: "Valeur : "  +  (sensor.value === undefined ? "Aucune" : sensor.value)};
              }

            }
          }
        }
        nodes[roomId] = roomNode;
      }

      this.oldNodes = nodes;

      return nodes;
    },

  },
};
</script>

<style>

#sidebar-left {
  margin-top: 10px;
}
#search-bar {
  margin: 10px;
}

</style>
