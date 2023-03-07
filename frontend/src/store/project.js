import {defineStore} from 'pinia'
import axios from "axios";

export const projectStore = defineStore('project', {
    state: () => ({
        currentProject: {},
        colors: {}
    }),
    getters: {
        getColors: (state) => state.colors
    },
    actions: {
        async updateProjectColors(colors) {
            let config = {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
            const response = await axios.put(`/api/bimiot/projects/colors/${this.currentProject.name}`, JSON.stringify(colors), config);
            this.colors = response.data.sensorsColorsApi;
        },
        async fetchSensorColors() {
            let config = {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
            this.colors  = await axios.get(`/api/bimiot/projects/colors/${this.currentProject.name}`, config);
            return this.colors;
        }
    }
})
