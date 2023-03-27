<template>
  <div class="fs-3 fw-bold mt-4 text-secondary">
    {{ boardPath }}
  </div>
  <div class="card bg-white p-4 mt-3">
    <table class="mt-3 table table-borderless">
      <tr>
        <th class="w-25 font-weight-bold text-primary">제목</th>
        <td>{{ article.title }}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">작성자</th>
        <td>{{ user.account }}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">조회수</th>
        <td>{{ article.views }}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">등록일</th>
        <td>{{ article.dateRegistered }}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">내용</th>
        <td>{{ article.content }}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">이미지</th>
        <td v-if="article.image !== undefined"><img :src="'data:image/jpg;base64,'+article.image"
                                                    width="240"
                                                    height="300"/></td>
        <td v-else>이미지 없음</td>
      </tr>
    </table>
    <div class="d-flex justify-content-start" name="buttons">
      <router-link :to="'/'+boardPath">
        <button class="btn btn-primary ms-2">
          목록
        </button>
      </router-link>
      <button class="ms-2 btn btn-danger" @click="deleteArticle" v-if="user.account === userLoggedIn">
        삭제
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "NoticeDetail"
}
</script>
<script setup>

import {inject} from "vue";
import apiClient from "/@/modules/apiUtil.js";

/**
 * ArticleDetail 에서 주입 받은 게시글 정보
 */
const article = inject('article');

/**
 * ArticleDetail 에서 주입 받은 게시판 경로
 */
const boardPath = inject('boardPath');

/**
 * ArticleDetail 에서 주입 받은 작성자 정보
 */
const user = inject('user');

/**
 * ArticleDetail 에서 주입 받은 로그인 회원 아이디
 */
const userLoggedIn = inject('userLoggedIn');

/**
 * 게시글 삭제 (작성자 회원에게만 보임)
 */
const deleteArticle = async () => {
  let response = await apiClient.delete(`${boardPath.value}/${article.value.id}`);
  if (response.success) {
    location.replace(`/${boardPath.value}`);
  }
}
</script>
<style scoped>

</style>