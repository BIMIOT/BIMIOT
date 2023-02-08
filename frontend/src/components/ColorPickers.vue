<template>
  <div>
    <div class="color-block" v-for="color in colors" :key="color.id" :style="{ background: color.value }" @click="openColorPicker(color.id)"></div>
    <v-app v-if="selectedColorId !== null" id="colorPicker">
      <v-tabs v-model="selectedTab">
        <v-tab>Color Picker</v-tab>
        <v-tab>Int List</v-tab>
      </v-tabs>
      <v-tab-item v-if="selectedTab === 0">
        <v-color-picker v-model="colors[selectedColorId].value" mode="hexa" @input="closeColorPicker"></v-color-picker>
      </v-tab-item>
      <v-tab-item v-if="selectedTab === 1">
        <div>
          <p>Int List</p>
          <ul>
            <li v-for="int in colors[selectedColorId].intList" :key="int">{{ int }}</li>
          </ul>
          <div>
            <v-text-field v-model="newInt" placeholder="Add a new int"></v-text-field>
            <v-btn @click="addInt(selectedColorId)">Add</v-btn>
          </div>
        </div>
      </v-tab-item>
    </v-app>
  </div>
</template>

<script>
export default {
  data() {
    return {
      colors: [
        { id: 0, value: '#ff0000', intList: [] },
        { id: 1, value: '#00ff00', intList: [] },
        { id: 2, value: '#0000ff', intList: [] },
        { id: 3, value: '#ffff00', intList: [] }
      ],
      selectedColorId: null,
      selectedTab: 0,
      newInt: ''
    }
  },
  mounted() {
    document.addEventListener('click', this.closeColorPickerOnClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.closeColorPickerOnClickOutside)
  },
  methods: {
    openColorPicker(id) {
      this.selectedColorId = id
    },
    closeColorPicker() {
      this.selectedColorId = null
    },
    closeColorPickerOnClickOutside(event) {
      if (!this.$el.contains(event.target) && this.selectedColorId !== null) {
        this.selectedColorId = null
      }
    },
    addInt(id) {
      this.colors[id].intList.push(Number(this.newInt))
      this.newInt = ''
    }
  }
}
</script>

<style>
.color-block {
  width: 50px;
  height: 50px;
  display: inline-block;
  cursor: pointer;
}
</style>
