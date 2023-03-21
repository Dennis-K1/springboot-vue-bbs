import {defineComponent} from "vue";
import ProfilePage from "/@/components/ProfilePage.vue";
import HomePage from "/@/components/HomePage.vue";

const NotFound = defineComponent({
  template : `<div>Not Found</div>`
});

const routes = [
  {path: '/', name: 'home', component: HomePage},
  {path: '/profile', name: 'profile', component: ProfilePage},
  {path: '/:catchAll(.*)+', component: NotFound}
]

export default routes;
