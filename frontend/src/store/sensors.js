import { defineStore } from 'pinia'

export const sensorsStore = defineStore('sensors', {
    state: () => ({
        colors: {}
    })
})
