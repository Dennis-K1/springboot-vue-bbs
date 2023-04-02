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
    meta: {
      boardPath: ['notice', 'inquiry', 'gallery', 'community']
    },
    children: [
      {
        path: '',
        component: ArticleList,
      },
      {
        path: ':id',
        component: ArticleDetail,
      },
      {
        path: 'form',
        component: ArticleInputForm,
        meta: {
          formPath: ['community', 'inquiry']
        }
      },
    ],
  },
  {
    path: '/profile', name: 'profile', component: ProfilePage,
    meta: {
      requiresLogin: true
    }
  },
  {
    path: '/signup', name: 'signup', component: SignupPage,
    meta: {
      requiresLogout: true
    }
  },
  {
    path: '/login', name: 'login', component: LoginPage,
    meta: {
      requiresLogout: true
    }
  },
  {path: '/', name: 'home', component: HomePage},
  {path: '/:catchAll(.*)', name: 'notFound', component: NotFound}
]

export default routes;
