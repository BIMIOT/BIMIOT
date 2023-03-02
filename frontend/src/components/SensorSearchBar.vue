<template v-slot:prepend-item>
  <div id="search-bar">
    <v-text-field
        density="compact"
        variant="solo"
        label="Search templates"
        append-inner-icon="mdi-magnify"
        single-line
        hide-details
        v-model="searchTerm"
        @input="searchFruits"
    ></v-text-field>
  </div>
</template>

<script>
export default {

  name: "SensorSearchBar",
  props : {
    room_list: {}
  },
  data() {
    return {
      searchTerm: ""
    }
  },
  methods: {
    searchFruits (e) {
      console.log(this.room_list[this.searchTerm])
    }
  }
}
</script>

<style scoped>
#search-bar {
  margin: 10px;
}
</style>
