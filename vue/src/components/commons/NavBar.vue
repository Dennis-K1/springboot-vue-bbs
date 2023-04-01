<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container-fluid">
    <a class="navbar-brand" href="/"> 포트폴리오 웹</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item" v-for="menu in leftMenus" :key="menu.key">
            <router-link :to="menu.url" class="nav-link">
              {{menu.value}}
            </router-link>
          </li>
        </ul>
        <ul v-if="isLoggedIn" class="navbar-nav">
          {{store.getters.getUser}}
          <li class="nav-item" v-for="menu in rightMenusAfterLogin" :key="menu.key">
            <router-link v-if="menu.key !== 'logout'" :to="menu.url" class="nav-link">
              {{menu.value}}
            </router-link>
            <a v-else @click="logout" class="nav-link" style="cursor:pointer">
              {{menu.value}}
            </a>
          </li>
        </ul>
        <ul v-else class="navbar-nav">
          <li class="nav-item" v-for="menu in rightMenusBeforeLogin" :key="menu.key">
            <router-link :to="menu.url" class="nav-link">
              {{menu.value}}
            </router-link>
          </li>
        </ul>
      </div>
  </div>
</nav>
</template>

<script>
export default {
  name: "NavBar"
}
</script>

<script setup>
import {computed} from "vue";
import {useStore} from "vuex";
import {useUser} from "/@/compositions/useUser.js";

/**
 * 로그아웃 펑션
 */
const {logout} = useUser();

/**
 * 유저 정보 저장소
 */
const store = useStore();

/**
 * 네비게이션 바 일반 메뉴 (왼쪽)
 */
const leftMenus = [
  {
    key: 'home', value: '홈', url: '/'
  },
  {
    key: 'notice', value: '공지사항', url: '/notice'
  },
  {
    key: 'community', value: '자유게시판', url: '/community'
  },
  {
    key: 'inquiry', value: '1:1문의', url: '/inquiry'
  },
  {
    key: 'gallery', value: '갤러리', url: '/gallery'
  }
]

/**
 * 네비게이션 바 로그인 전 메뉴 (오른쪽)
 */
const rightMenusBeforeLogin = [
  {
    key: 'login', value: '로그인', url:'/login'
  },
  {
    key: 'signup', value: '회원가입', url:'/signup'
  }
]

/**
 * 네비게이션 바 로그인 후 메뉴 (오른쪽)
 */
const rightMenusAfterLogin = [
  {
    key: 'profile', value: '마이페이지', url: '/profile'
  },
  {
    key: 'logout', value: '로그아웃', url: '/logout'
  },
]

/**
 * Store 에 userAccount 가 저장되어 있는지 여부로 유저 로그인 여부 확인
 */
const isLoggedIn = computed(() => {
  return store.state.userAccount !== null;
});

</script>

<style scoped>

</style>