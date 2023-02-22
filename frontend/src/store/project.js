import {defineStore} from 'pinia'
import axios from "axios";

export const projectStore = defineStore('project', {
    state: () => ({
        currentProjectName: null,
        colors: {}
    }),
    actions: {
        async updateProjectColors(colors){
            let config = {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
            const response = await axios.put(`/api/bimiot/projects/colors/${this.currentProjectName}`, JSON.stringify(colors), config);
            this.colors = response.data.sensorsColorsApi;
        },
        async fetchSensorColors(){
            let config = {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
            const response = await axios.get(`/api/bimiot/projects/colors/${this.currentProjectName}`, config);
            this.colors = response.data;
            console.log(this.colors);
        }
    }
})
