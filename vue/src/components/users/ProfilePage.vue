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
    <div v-if="articleList !== []" class="card bg-white p-4 mt-3">
      <table class="mt-3 table table-borderless">
        <tr class="font-weight-bold text-primary">
          <th scope="col">제목</th>
          <th scope="col">조회수</th>
          <th scope="col">등록일</th>
        </tr>
        <tr v-for="article in articleList" :key="article.id">
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

import {onMounted, ref} from "vue";
import apiClient from "/@/modules/apiUtil.js";

/**
 * 회원 정보
 */
const user = ref({});

/**
 * 회원 작성 게시글 목록
 */
const articleList = ref([]);

/**
 * 회원 정보 조회
 */
async function fetchData() {
  let response = await apiClient.get('users/profile');
  articleList.value = response.data.articleList;
  user.value = response.data.user;
}

onMounted(() => {
  fetchData();
})

</script>
<style scoped>

</style>