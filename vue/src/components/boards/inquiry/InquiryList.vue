<template>
  <main class="container mb-5">
    <div class="table-responsive">
      <table class="table text-center table-borderless">
        <thead>
        <tr class="font-weight-bold text-primary">
          <th>작성자</th>
          <th>제목</th>
          <th>답변 여부</th>
          <th>등록일</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="article in articleList" :key="article.id">
          <td>{{ article.user.account }}</td>
          <td class="text-start">
            <router-link :to="`/${boardPath}/${article.id}`">
              {{ article.title }}
            </router-link>
          </td>
          <td v-if="article.replyList.length > 0" class="text-primary text-center">답변 완료</td>
          <td v-else class="text-danger text-center">미답변</td>
          <td>{{ article.dateRegistered }}</td>
        </tr>
        </tbody>
      </table>
      <div class="d-flex justify-content-end">
        <button @click="authenticate(`/${boardPath}/form`)"
                class="btn btn-sm btn-primary" type="button">
          게시글 등록
        </button>
      </div>
    </div>
  </main>
</template>

<script>
export default {
  name: "InquiryList"
}
</script>
<script setup>

import {inject} from "vue";
import {useValidation} from "/@/compositions/useValidation.js";

const {authenticate} = useValidation();

/**
 * ArticleList 에서 주입 받는 게시글 목록
 */
const articleList = inject('articleList');

/**
 * ArticleList 에서 주입 받는 경로명
 */
const boardPath = inject("boardPath");

</script>

<style scoped>

</style>