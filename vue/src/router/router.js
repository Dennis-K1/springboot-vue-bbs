import {createRouter, createWebHistory} from "vue-router";
import routes from "/@/router/routes.js";
import store from "/@/modules/store.js";

export const router = createRouter({
  history: createWebHistory(),
  linkActiveClass: 'active',
  routes
});


router.beforeEach(async (to, from, next) => {

  /**
   * 전체 경로
   */
  const toPath = to.path;

  /**
   * 로그인 필요 페이지 메타
   */
  const requiresLogin = to.matched.some(record => record.meta.requiresLogin);

  /**
   * 로그아웃 필요 페이지 메타
   */
  const requiresLogout = to.matched.some(record => record.meta.requiresLogout);

  /**
   * /:path/form 메타 및 해당하는 게시판 경로 배열
   */
  const formPath = to.meta.formPath;

  /**
   * 게시판 경로
   */
  const boardPath = to.meta.boardPath;

  /**
   * 로그인 하지 않은 채로 로그인 후 가능 메뉴에 접근 할 시
   */
  if (!store.state.token && requiresLogin) {
    alert('로그인 후 이용할 수 있습니다.');
    next(`/login?redirectURL=${toPath}`);
    return;
  }

  /**
   * 로그인 후 로그인 전 가능 메뉴에 접근 할 시 인덱스 페이지로
   */
  if (store.state.token && requiresLogout) {
    next('/');
    return;
  }

  /**
   * 입력 가능 게시판이 아닌 게시판 경로로 접근 시 게시판 목록으로 (예: /notice/form)
   */
  if (formPath && !formPath.includes(to.params.path)) {
    next(`/${to.params.path}`);
    return;
  }

  /**
   * 해당하지 않는 게시판 경로로 접근 시
   */
  if (boardPath && !boardPath.includes(to.params.path)) {
    alert('존재하지 않는 페이지입니다.')
    next('/');
    return;
  }
  next();
})