import { defineStore } from 'pinia'
import {ref} from "vue";

export const roomsStateStore = defineStore('rooms', () => {
    const state =  ref({})
    function getLastRoomColorByType (roomId,sensorType) {
        let combinedKey = `${roomId},${sensorType}`;
        return state.value[combinedKey];
    }

    function storeNewRoomColorByType(roomId,sensorType, value) {
        let combinedKey = `${roomId},${sensorType}`;
        state.value[combinedKey] = value;
    }
    return {state, getLastRoomColorByType, storeNewRoomColorByType}

})
