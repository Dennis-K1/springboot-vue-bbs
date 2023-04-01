import {createRouter, createWebHistory} from "vue-router";
import routes from "/@/router/routes.js";
import store from "/@/modules/store.js";

export const router = createRouter({
  history: createWebHistory(),
  linkActiveClass: 'active',
  routes
});

/**
 * 게시판 해당 경로
 */
const boardPaths = ['notice', 'inquiry', 'gallery', 'community'];

/**
 * 입력폼 가능 게시판 경로
 */
const formPaths = ['community', 'inquiry'];

/**
 * 로그인 전 표시 가능 메뉴
 */
const beforeLogin = ['/signup', '/login'];

/**
 * 로그인 후 가능 메뉴
 */
const afterLogin = ['/profile', '/logout']

router.beforeEach(async (to, from, next) => {

  /**
   * 전체 경로
   */
  const toPath = to.path;

  /**
   * 첫 번째 경로
   */
  const path = to.params.path;

  /**
   * 사용자 로그인 시 발행되는 jwt
   */
  const jwt = store.state.token;

  /**
   * 로그인 하지 않은 채로 로그인 후 가능 메뉴에 접근 할 시
   */
  if (jwt === null && afterLogin.includes(toPath)) {
    alert('로그인 후 이용할 수 있습니다.');
    next(`/login?redirectURL=${toPath}`);
    return;
  }

  /**
   * 로그인 후 로그인 전 가능 메뉴에 접근 할 시
   */
  if (jwt !== null && beforeLogin.includes(toPath)) {
    next('/');
    return;
  }

  /**
   * 입력 가능 게시판이 아닌 게시판 경로로 접근 시
   */
  if (boardPaths.includes(path)) {
    if (toPath.split('/')[2] === 'form' && !formPaths.includes(toPath.split('/')[1])) {
      next(path);
      return;
    }
  }
  next();
})