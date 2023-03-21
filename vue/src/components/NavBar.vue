<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="/"> 포트폴리오 웹</a>
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
import {computed} from "vue";

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
]

const leftMenus = computed(() => menus.filter((menu) => menu.position === 'left'));
const rightMenus = computed(() => menus.filter((menu) => menu.position === 'right'));
const menuCategory = [
  {
    id: 1, me_auto:true, value: leftMenus.value
  },
  {
    id: 2, me_auto:false, value: rightMenus.value
  },
]
</script>

<style scoped>

</style>