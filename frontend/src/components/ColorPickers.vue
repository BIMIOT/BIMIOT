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
            <li v-for="int in values" :key="int">{{ int }}</li>
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
  name: "ColorPickers",
  props: {
    datas: []
  },
  watch: {
    colors: {
      handler: function(newValue) {
        this.$emit('colors',newValue);
      },
      deep: true
    },
    values: {
      handler: function(newValue) {
        this.$emit('values',newValue);
      },
      deep: true
    }
  },
  data() {
    return {
      colors: [
        { id: 0, value: '#ff0000' },
        { id: 1, value: '#00ff00' },
        { id: 2, value: '#0000ff' },
        { id: 3, value: '#ffff00' }
      ],
      values: [3,10,20],
      selectedColorId: null,
      selectedTab: 0,
      newInt: ''
    }
  },
  mounted() {
    this.$emit('colors',this.colors);
    this.$emit('values',this.values);
    console.log("hello")
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
      this.values.push(Number(this.newInt))
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
