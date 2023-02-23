import {defineStore} from 'pinia'
import axios from "axios";

export const projectStore = defineStore('project', {
    state: () => ({
        currentProjectName: null,
        colors: {
            "TEMPERATURE": [{"colorCode": "#7F00FF", "threshold": -10}, {
                "colorCode": "#00FFFF",
                "threshold": 20
            }, {"colorCode": "#80FF00", "threshold": 30}, {
                "colorCode": "#FE0000",
                "threshold": Infinity
            }],
            "LIGHT": [{"colorCode": "#000000", "threshold": 1}, {
                "colorCode": "#6D6D00",
                "threshold": 30
            }, {"colorCode": "#B6B600", "threshold": 60}, {
                "colorCode": "#FFFF00",
                "threshold": Infinity
            }],
            "HUMIDITY": [{"colorCode": "#FE0000", "threshold": 1}, {
                "colorCode": "#80FF00",
                "threshold": 40
            }, {"colorCode": "#00FFFF", "threshold": 70}, {
                "colorCode": "#0000FF",
                "threshold": Infinity
            }],
            "CO2": [{"colorCode": "#CFFF2E", "threshold": 1100}, {
                "colorCode": "#FFE72E",
                "threshold": 1400
            }, {"colorCode": "#FF9F2E", "threshold": 5000}, {
                "colorCode": "#FF582E",
                "threshold": Infinity
            }]
        }
    }),
    actions: {
        async updateProjectColors(colors) {
            let config = {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
            const response = await axios.put(`/api/bimiot/projects/colors/${this.currentProjectName}`, JSON.stringify(colors), config);
            this.colors = response.data.sensorsColorsApi;
            console.log(this.colors);
        },
        async fetchSensorColors() {
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
