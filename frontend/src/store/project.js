import {defineStore} from 'pinia'

export const projectStore = defineStore('project', {
    state: () => ({
        currentProjectName: null
    })
})
