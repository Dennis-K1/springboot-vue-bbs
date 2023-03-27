<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
  <div class="container-fluid">
    <a class="navbar-brand"> 포트폴리오 웹</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul :class="{'navbar-nav':true, 'me-auto':menu.me_auto}"
            v-for="menu in menuCategory"
            :key="menu.id">
          <li class="nav-item" v-for="menu in menu.value" :key="menu.key">
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
import {computed, inject, ref} from "vue";

/**
 * jwt 로 로그인 여부 확인
 */
const isLoggedIn = ref(localStorage.getItem('jwt'));

/**
 * 네비게이션 바 메뉴, position:표시 위치
 */
const menus = [
  {
    key: 'home', value: '홈', url: '/', position: 'left'
  },
  {
    key: 'notice', value: '공지사항', url: '/notice', position: 'left'
  },
  {
    key: 'community', value: '자유게시판', url: '/community', position: 'left'
  },
  {
    key: 'inquiry', value: '1:1문의', url: '/inquiry', position: 'left'
  },
  {
    key: 'gallery', value: '갤러리', url: '/gallery', position: 'left'
  },
  {
    key: 'profile', value: '마이페이지', url: '/profile', position: 'right'
  },
  {
    key: 'logout', value: '로그아웃', url: '/logout', position: 'right'
  },
  {
    key: 'login', value: '로그인', url:'/login', position: 'right'
  },
  {
    key: 'signup', value: '회원가입', url:'/signup', position: 'right'
  }
]

/**
 * 왼쪽 표시 메뉴
 */
const leftMenus = computed(() => menus.filter((menu) => menu.position === 'left'));

/**
 * 오른쪽 표시 메뉴
 */
const rightMenus = computed(() => {
  if (isLoggedIn.value !== null) {
    return menus.filter((menu) => menu.key === 'profile' || menu.key === 'logout');
  } else {
    return menus.filter((menu) => menu.key === 'login' || menu.key === 'signup');
  }
});

/**
 * 메뉴 모음
 */
const menuCategory = [
  {
    id: 1, me_auto:true, value: leftMenus.value
  },
  {
    id: 2, me_auto:false, value: rightMenus.value
  },
];


</script>

<style scoped>

</style>