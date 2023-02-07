import { createRouter, createWebHistory } from 'vue-router'
import createProject from "@/components/CreateProject.vue";
import ModelViewer from "@/components/ModelViewer.vue";
import Error404 from "@/components/Error404.vue";

export default createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: createProject,
        },
        {
            path: '/simulation',
            name:'simulation',
            component: ModelViewer,
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'error-404',
            component: Error404
        }
    ],
})
