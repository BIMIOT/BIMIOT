import { defineStore } from 'pinia';

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
