<template>
  <div class="color-block-container">
    <div class="color-block" v-for="(color,index) in colorToValue"
         :key="index"
         :style="{ background: color.colorCode }"
         @click="openColorPicker(index)">

    </div>
    <v-app :class="selectedTab === 0 ? 'color-picker-container-colors' : 'color-picker-container-values'" :style="{
        position: 'absolute',
        transform: 'translateX(-45px)'

      }" v-if="selectedColorIndex !== null" id="colorPicker">
      <v-tabs v-model="selectedTab">
        <v-tab>Color Picker</v-tab>
        <v-tab>Interval List</v-tab>
      </v-tabs>

      <v-tab-item class="tab-item" v-if="selectedTab === 0">
        <v-color-picker v-model="colorToValue[selectedColorIndex].colorCode" mode="hexa"
                        @input="closeColorPicker"></v-color-picker>

      </v-tab-item>

      <v-tab-item v-if="selectedTab === 1">
        <div class="value-inputs">
          <p>Modify the interval</p>
          <v-text-field
              v-for="(value,index) in colorToValue.slice(0,3)"
              :key="index"
              v-model="value.threshold"
              :placeholder="value.threshold"
              type="number"
              :rules="[v => v <= this.colorToValue[index+1].threshold ||  'Le seuil doit être inferieur ou égal au suivant']"
              validate-on="input"
          />
        </div>
      </v-tab-item>
      <v-btn class="primary" v-on:click="save()">Save</v-btn>
      <v-btn v-on:click="cancel()">Cancel</v-btn>

    </v-app>
  </div>
</template>

<script>

import {projectStore} from "@/store/project";

export default {
  name: "ColorPickers",
  props: {
    sensorType: {}
  },
  watch: {
    colorToValue: {
      handler: function (newValue) {
        this.$emit('colorToValue', newValue)
      },
      deep: true
    }
  },
  setup() {
    const store = projectStore();
    return {store}
  },
  data() {
    return {
      selectedColorIndex: null,
      selectedTab: 0,
      colorToValue: [
        {
          colorCode: '#ff0000',
          threshold: 3
        },
        {
          colorCode: '#00ff00',
          threshold: 10
        },
        {
          colorCode: '#0000ff',
          threshold: 20
        },
        {
          colorCode: '#ffff00',
          threshold: Infinity
        }
      ]
    }
  },

  mounted() {
    this.store.fetchSensorColors().then(() => {
      const v = this.store.getColors.data
      this.colorToValue = v[this.sensorType];
    });

    this.$emit('colorToValue', this.colorToValue)
    console.log("colorToValue", this.colorToValue)
    document.addEventListener('click', this.closeColorPickerOnClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.closeColorPickerOnClickOutside)
  },
  methods: {
    save() {
      this.$emit('save', "save");
      this.closeColorPicker();
    },
    cancel() {
      this.closeColorPicker();
      this.store.fetchSensorColors().then(()=>{
          const v = this.store.getColors.data;
          this.colorToValue = v[this.sensorType];
      });
    },
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
  }
}
</script>

<style>

.color-block-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
}

.color-picker-container-colors {
  position: absolute;
  top: 450%;
  left: 0;
  align-items: center;
}

.color-picker-container-values {
  position: absolute;
  top: 375%;
  left: 0;
  align-items: center;
}

.color-block {
  width: 50px;
  height: 18px;
  cursor: pointer;
  display: flex;
  justify-content: center;
}


.tab-item {
  margin-bottom: 18px;
}


.v-application__wrap {
  background-color: white;
  backface-visibility: hidden;
  display: flex;
  flex-direction: column;
  flex: 1 1 auto;
  max-width: 100%;
  min-height: 0vh;
  position: relative;
}

.primary {
  color: white;
  background-color: #023D57;
}
</style>
