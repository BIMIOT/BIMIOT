<template>
  <div>
    <div class="color-block" v-for="(color,index) in colorToValue"
         :key="index"
         :style="{ background: color.colorCode }"
         @click="openColorPicker(index)">

    </div>
    <v-app v-if="selectedColorIndex !== null" id="colorPicker">
      <v-tabs v-model="selectedTab">
        <v-tab>Color Picker</v-tab>
        <v-tab>Interval List</v-tab>
      </v-tabs>
      <v-tab-item v-if="selectedTab === 0">
        <v-color-picker v-model="colorToValue[selectedColorIndex].colorCode" mode="hexa" @input="closeColorPicker"></v-color-picker>
      </v-tab-item>
      <v-tab-item v-if="selectedTab === 1">
        <div>
          <p>Modify the interval</p>

          <v-text-field
              v-for="(value,index) in colorToValue.slice(0,3)"
              :key="index"
              v-model="value.threshold"
              :placeholder="value.threshold"
              type="number"
              :rules= "[v => v <= this.colorToValue[index+1].threshold ||  'Le seuil doit inferieur ou égal à suivant']"
              validate-on="input"
          />

<!--          <div>-->
<!--            <v-text-field v-model="newInt" placeholder="Add a new int"></v-text-field>-->
<!--            <v-btn @click="addInt(selectedColorId)">Add</v-btn>-->
<!--          </div>-->
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
    colorToValue: {
      handler:function (newValue){
        this.$emit('colorToValue',newValue)
      },
      deep:true
    }
  },
  data() {
    return {
      selectedColorIndex: null,
      selectedTab: 0,
      colorToValue:[
        {
          colorCode:'#ff0000',
          threshold: 3
        },
        {
          colorCode:'#00ff00',
          threshold: 10
        },
        {
          colorCode:'#0000ff',
          threshold: 20
        },
        {
          colorCode:'#ffff00',
          threshold: Infinity
        }
      ]
    }
  },
  mounted() {

    this.$emit('colors',this.colors);
    this.$emit('values',this.values);
    this.$emit('colorToValue',this.colorToValue)
    console.log("hello")
    console.log("colorToValue",this.colorToValue)
    document.addEventListener('click', this.closeColorPickerOnClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.closeColorPickerOnClickOutside)
  },
  methods: {
    openColorPicker(id) {
      this.selectedColorIndex = id
    },
    closeColorPicker() {
      this.selectedColorIndex = null
    },
    closeColorPickerOnClickOutside(event) {
      if (!this.$el.contains(event.target) && this.selectedColorIndex !== null) {
        this.selectedColorIndex = null
      }
    },
    updateInterval(index,value) {
      console.log("!!!!values are: ",value)
    }
  }
}
</script>

<style>
.color-block {
  width: 50px;
  height: 24px;
  display: inline-block;
  cursor: pointer;
}
</style>
