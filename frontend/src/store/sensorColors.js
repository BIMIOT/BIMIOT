import { defineStore } from 'pinia'
import {ref} from "vue";

export const sensorColorsStateStore = defineStore('sensorsValues', () => {
    const state =  ref({})
    function getSensorValue (sensorType) {
        return state.value[`${sensorType}`];
    }
    function storeNewSensorValue(sensorType,value) {
        state.value[`${sensorType}`] = value;
    }
    return {state, storeNewSensorValue, getSensorValue}
})
