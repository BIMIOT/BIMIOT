<template>
  <div>
    <div class="color-block" v-for="(color,index) in colorToValue"
         :key="index"
         :style="{ background: color.colorHex }"
         @click="openColorPicker(index)">

    </div>
    <v-app v-if="selectedColorIndex !== null" id="colorPicker">
      <v-tabs v-model="selectedTab">
        <v-tab>Color Picker</v-tab>
        <v-tab>Interval List</v-tab>
      </v-tabs>
      <v-tab-item v-if="selectedTab === 0">
        <v-color-picker v-model="colorToValue[selectedColorIndex].colorHex" mode="hexa" @input="closeColorPicker"></v-color-picker>
      </v-tab-item>
      <v-tab-item v-if="selectedTab === 1">
        <div>
          <p>Modify the interval</p>
<!--          <v-text-field-->
<!--              v-model="colorToValue[0].end"-->
<!--              :placeholder="colorToValue[0].end"-->
<!--              type="number"-->
<!--          />-->
<!--          <v-text-field-->
<!--              v-model="colorToValue[1].end"-->
<!--              :placeholder="colorToValue[1].end"-->
<!--              type="number"-->
<!--          />-->
<!--          <v-text-field-->
<!--              v-model="colorToValue[2].end"-->
<!--              :placeholder="colorToValue[2].end"-->
<!--              type="number"-->
<!--          />-->

          <v-text-field
              v-for="(value,index) in colorToValue.slice(0,3)"
              :key="index"
              v-model="value.end"
              :placeholder="value.end"
              type="number"
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
    },
    colorToValue: {
      handler:function (newValue){
        this.$emit('colorToValue',newValue)
      },
      deep:true
    }
  },
  data() {
    return {
      // colors: [
      //   { id: 0, value: '#ff0000' },
      //   { id: 1, value: '#00ff00' },
      //   { id: 2, value: '#0000ff' },
      //   { id: 3, value: '#ffff00' }
      // ],
      // values: [0,3,10,20],
      selectedColorIndex: null,
      selectedTab: 0,
      colorToValue:[
        {
          colorHex:'#ff0000',
          start: null,
          end: 3
        },
        {
          colorHex:'#00ff00',
          start: 3,
          end: 10
        },
        {
          colorHex:'#0000ff',
          start: 10,
          end: 20
        },
        {
          colorHex:'#ffff00',
          start: 20,
          end: null
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
