import ProfilePage from "/@/components/users/ProfilePage.vue";
import HomePage from "/@/components/HomePage.vue";
import BoardContainer from "/@/components/boards/BoardContainer.vue";
import NotFound from "/@/components/commons/NotFound.vue";
import ArticleDetail from "/@/components/boards/ArticleDetail.vue";
import ArticleInputForm from "/@/components/boards/ArticleInputForm.vue";
import ArticleList from "/@/components/boards/ArticleList.vue";
import LoginPage from "/@/components/users/LoginPage.vue";
import SignupPage from "/@/components/users/SignupPage.vue";
import LogoutPage from "/@/components/users/LogoutPage.vue";

import apiClient from "/@/modules/apiUtil.js";

const boardNames = ['notice', 'inquiry', 'gallery', 'community'];

const routes = [
  {
    path: '/:path',
    name: 'board',
    component: BoardContainer,
    /**
     * 게시판명에 해당하는 경우만 넘김
     */
    beforeEnter: (to, from, next) => {
      const path = to.params.path;
      if (boardNames.includes(path)) {
        next()
      }
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
        /**
         * 게시판명에 해당하는 경우만 넘김
         */
        beforeEnter: (to, from, next) => {
          const path = to.params.path;
          if (path === 'community' || path === 'inquiry') {
            next()
          } else {
            next(false)
          }
        }
      },
    ],
  },

  {
    path: '/profile', name: 'profile', component: ProfilePage,
    /**
     * 비로그인 회원이 경로로 진입하지 못하게 처리
     */
    beforeEnter: (to, from, next) => {
      const jwt = localStorage.getItem('jwt');
      if (jwt === null) {
        alert('로그인이 필요합니다.')
        location.replace('/');
      }
      next()
    }
  },
  {path: '/signup', name: 'signup', component: SignupPage},
  {
    path: '/logout', name: 'logout', component: LogoutPage,
    /**
     * 로그아웃 경로 진입 전 로그아웃 처리 진행
     */
    beforeEnter: async (to, from, next) => {
      const jwt = localStorage.getItem('jwt');
      if (jwt === null) {
        location.replace('/');
      }
      let response = await apiClient.post('users/logout', null);
      if (response.success) {
        localStorage.removeItem('jwt');
        localStorage.removeItem('userLoggedIn');
        location.replace('/');
      }
      next('/');
    }
  },
  {
    path: '/login', name: 'login', component: LoginPage,
    /**
     * 로그인 후 뒤로가기 시 로그인 전 화면으로
     */
    beforeEnter: (to, from, next) => {
      const jwt = localStorage.getItem('jwt');
      if (jwt !== null) {
        location.replace(from.path);
      }
      next()
    }
  },
  {path: '/', name: 'home', component: HomePage},
  //  404 NotFound
  {path: '/:catchAll(.*)', name: 'notFound', component: NotFound}
]

export default routes;
