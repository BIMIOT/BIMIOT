import { createRouter, createWebHistory } from 'vue-router'
import createProject from "@/components/CreateProject.vue";
import ModelViewer from "@/components/ModelViewer.vue";

export default createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: createProject,
        },
        {
            path: '/simulation',
            component: ModelViewer,
        },
    ],
})
