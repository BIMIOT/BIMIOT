import { defineStore } from 'pinia'
import {ref} from "vue";

export const unitsTypeStore = defineStore('units', {
    state: () => ({
        units: {
            "TEMPERATURE":" °C",
            "LIGHT":" Lux",
            "HUMIDITY":" %",
            "CO2":" PPM",
        },
        }),
        actions: {
            getUnitFromType(type) {
                return this.units[type];
            }
        }

})
