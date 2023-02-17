import { defineStore } from 'pinia'
import {ref} from "vue";

export const sensorValueStateStore = defineStore('sensorsValues', () => {
    const state =  ref({})
    function getSensorValue (sensorId) {
        return state.value[`${sensorId}`];
    }
    function storeNewSensorValue(sensorId,value) {
        state.value[`${sensorId}`] = value;
    }
    return {state, storeNewSensorValue, getSensorValue}
})
