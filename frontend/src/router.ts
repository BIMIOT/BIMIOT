import { createRouter, createWebHistory } from 'vue-router'
import createProject from "@/views/CreateProject.vue";
import ModelViewer from "@/components/ModelViewer.vue";
import Error404 from "@/components/Error404.vue";
import HomeProjectsPage from "@/views/HomeProjectsPage.vue";

export default createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeProjectsPage,
        },
        {
            path: '/create-project',
            name: 'create-project',
            component: createProject
        },
        {
            path: '/simulation/:project',
            name:'simulation',
            component: ModelViewer,
            props: true
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'error-404',
            component: Error404
        }
    ],
})
