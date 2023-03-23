import ProfilePage from "/@/components/users/ProfilePage.vue";
import HomePage from "/@/components/HomePage.vue";
import BoardContainer from "/@/components/boards/BoardContainer.vue";
import NotFound from "/@/components/commons/NotFound.vue";
import ArticleDetail from "/@/components/boards/ArticleDetail.vue";


const boardNames = ['notice', 'inquiry', 'gallery', 'community'];

const routes = [
  {path: '/', name: 'home', component: HomePage},
  {path: '/profile', name: 'profile', component: ProfilePage},
  {path: '/:path',
    name: 'board',
    component: BoardContainer,
    beforeEnter: (to, from, next) => {
    const path = to.params.path;
    //게시판명에 해당하지 않는 경우 넘김
    if (boardNames.includes(path)) {
      next()
    }
  }},
  {path: '/:path/:id',
  name:'detail',
  component: ArticleDetail,
    beforeEnter: (to, from, next) => {
      const path = to.params.path;
      //게시판명에 해당하지 않는 경우 넘김
      if (boardNames.includes(path)) {
        next()
      }
      }},
  //  404 NotFound
  {path: '/:catchAll(.*)*', name: 'notFound', component: NotFound}
]

export default routes;
