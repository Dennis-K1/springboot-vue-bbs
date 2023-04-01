<template>
  <main class="container mb-5">
    <div class="fs-3 fw-bold mt-4 text-secondary">
      회원 정보
    </div>
    <div class="card bg-white p-4 mt-3">
      <table class="mt-3 table table-borderless">
        <tr>
          <th class="w-25 font-weight-bold text-primary">아이디</th>
          <td>{{ user.account }}</td>
        </tr>
        <tr>
          <th class="w-25 font-weight-bold text-primary">가입일</th>
          <td>{{ user.dateRegistered }}</td>
        </tr>
        <tr>
          <th class="w-25 font-weight-bold text-primary">등록 게시글수</th>
          <td>{{ user.countArticle }}</td>
        </tr>
      </table>
    </div>
    <div class="fs-3 fw-bold mt-4 text-secondary">
      작성한 게시글 목록
    </div>
    <div v-if="userArticleList !== []" class="card bg-white p-4 mt-3">
      <table class="mt-3 table table-borderless">
        <tr class="font-weight-bold text-primary">
          <th scope="col">제목</th>
          <th scope="col">조회수</th>
          <th scope="col">등록일</th>
        </tr>
        <tr v-for="article in userArticleList" :key="article.id">
          <td>{{ article.title }}</td>
          <td>{{ article.views }}</td>
          <td>{{ article.dateRegistered }}</td>
        </tr>
      </table>
    </div>
    <div v-else>
      작성한 게시글 없음
    </div>
  </main>

</template>

<script>

export default {
  name: "Profile",
}
</script>
<script setup>

import {onMounted} from "vue";
import {useUser} from "/@/compositions/useUser.js";

/**
 *   user: 회원 정보
 *   userArticleList: 회원 작성 게시글 목록
 *   getProfile: 정보 조회
 */
const {
  user,
  userArticleList,
  getProfile
} = useUser();

onMounted(async () => {
  await getProfile();
})

</script>
<style scoped>

</style>