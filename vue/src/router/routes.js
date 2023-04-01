import ProfilePage from "/@/components/users/ProfilePage.vue";
import HomePage from "/@/components/HomePage.vue";
import BoardContainer from "/@/components/boards/BoardContainer.vue";
import NotFound from "/@/components/commons/NotFound.vue";
import ArticleDetail from "/@/components/boards/ArticleDetail.vue";
import ArticleInputForm from "/@/components/boards/ArticleInputForm.vue";
import ArticleList from "/@/components/boards/ArticleList.vue";
import LoginPage from "/@/components/users/LoginPage.vue";
import SignupPage from "/@/components/users/SignupPage.vue";

const routes = [
  {
    path: '/:path',
    name: 'board',
    component: BoardContainer,
    children: [
      {
        path: '',
        component: ArticleList,
      },
      {
        path: ':id',
        component: ArticleDetail
      },
      {
        path: 'form',
        component: ArticleInputForm,
      },
    ],
  },
  {
    path: '/profile', name: 'profile', component: ProfilePage,
  },
  {path: '/signup', name: 'signup', component: SignupPage},
  {
    path: '/login', name: 'login', component: LoginPage,
  },
  {path: '/', name: 'home', component: HomePage},
  {path: '/:catchAll(.*)', name: 'notFound', component: NotFound}
]

export default routes;
