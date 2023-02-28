import { defineStore } from 'pinia'
import {ref} from "vue";

export const roomsStateStore = defineStore('rooms', {
    state: () => ({
        hashmap: {},
        }),
        actions: {
            getLastRoomColorByType(roomId, sensorType) {
                let combinedKey = `${roomId},${sensorType}`;
                return this.hashmap[combinedKey];
            },
            storeNewRoomColorByType(roomId, sensorType, value) {
                let combinedKey = `${roomId},${sensorType}`;
                this.hashmap[combinedKey] = value;
            }
        }

})
