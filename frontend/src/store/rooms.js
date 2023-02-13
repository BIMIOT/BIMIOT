import { defineStore } from 'pinia'
import {computed} from "vue";

export const roomsStateStore = defineStore('rooms', () => {
    const state =  ref({})
    const getLastRoomColorByType = computed((room,type) => { return "" });
    const storeNewRoomColorByType =  computed((room,type, value) => { return "" });
    return {state, getLastRoomColorByType, storeNewRoomColorByType}

})
