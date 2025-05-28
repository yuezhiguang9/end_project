import {createRouter, createWebHistory} from "vue-router";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "searchstudent",
            component: () => import("@/views/searchstudent.vue"),
        },
        {
            path: "/searchcourse",
            name: "searchcourse",
            component: () => import("@/views/searchcourse.vue"),
        },
        {
            path: "/searchsc",
            name: "searchsc",
            component: () => import("@/views/searchsc.vue"),
        },
        {
            path: "/searchgrade",
            name: "searchgrade",
            component: () => import("@/views/searchgrade.vue"),
        },
        {
            path: "/uploadFile",
            name: "uploadFile",
            component: () => import("@/views/uploadFile.vue")
        }
    ],
});

export default router;
