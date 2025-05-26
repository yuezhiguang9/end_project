import { createRouter, createWebHistory } from "vue-router";

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
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("@/views/searchcourse.vue"),
    },
    {
      path: "/searchsc",
      name: "searchsc",
      component: () => import("@/views/searchsc.vue"),
    },
    {
      path: "/revisestudent",
      name: "revisestudent",
      component: () => import("@/views/revisestudent.vue"),
    },
    {
      path: "/revisecourse",
      name: "revisecourse",
      component: () => import("@/views/revisecourse.vue"),
    },
    {
      path: "/revisesc",
      name: "revisesc",
      component: () => import("@/views/revisesc.vue"),
    },
  ],
});

export default router;
