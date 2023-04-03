<template>
  <div class="fs-3 fw-bold mt-4 text-secondary">
    {{ boardPath }}
  </div>
  <div class="card bg-white p-4 mt-3">
    <table class="mt-3">
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
        <td v-if="article.image !== undefined">
          <div class="image-container">
            <img :src="'data:image/jpg;base64,'+article.image" class="article-image"/>
          </div>
        </td>
        <td v-else>이미지 없음</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">답변 여부</th>
        <td v-if="replyList.length > 0">답변 완료</td>
        <td v-else>미답변</td>
      </tr>
      <tr v-if="replyList.length !== 0">
        <th class="w-25 font-weight-bold text-primary d-flex">답변</th>
        <td class="reply">
          {{ replyList[0].content }}
        </td>
      </tr>
      <div class="d-flex justify-content-start" name="buttons">
        <router-link :to="'/'+boardPath">
          <button class="btn btn-primary mt-2">
            목록
          </button>
        </router-link>
        <button class="ms-2 mt-2 btn btn-danger" @click="deleteArticle(boardPath, article.id)" v-if="user.account === userLoggedIn">
          삭제
        </button>
      </div>
    </table>
  </div>
</template>

<script>
export default {
  name: "InquiryDetail"
}
</script>
<script setup>

import {computed, inject} from "vue";
import {useBoard} from "/@/compositions/useBoard.js";
import {useStore} from "vuex";

const {deleteArticle} = useBoard();

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
 * ArticleDetail 에서 주입 받은 댓글 목록
 */
const replyList = inject('replyList');

/**
 * 로그인된 회원 아이디
 */
const store = useStore();
const userLoggedIn = computed(()=>{
  return store.getters.getUser;
})

</script>
<style scoped>
.image-container {
  width: 240px;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.article-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}
</style>