<template>
  <div class="fs-3 fw-bold mt-4 text-secondary">
    {{ boardPath }}
  </div>
  <div class="card bg-white p-4 mt-3">
    <table v-if="isArticleDetail === 1" class="mt-3 table table-borderless">
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
    </table>
    <template v-else>
      <ArticleEditForm/>
    </template>
    <div class="d-flex justify-content-start" name="buttons">
      <router-link :to="'/'+boardPath">
        <button class="btn btn-primary ms-2">
          목록
        </button>
      </router-link>
      <button class="ms-2 btn btn-danger" @click="deleteArticle(boardPath, article.id)" v-if="user.account === userLoggedIn">
        삭제
      </button>
      <button class="ms-2 btn btn-danger" @click="isArticleDetail = 0" v-if="user.account === userLoggedIn && isArticleDetail === 1">
        수정
      </button>
      <button class="ms-2 btn btn-danger" @click="isArticleDetail = 1" v-else-if="user.account === userLoggedIn && isArticleDetail === 0">
        수정 취소
      </button>
    </div>
  </div>
</template>

<script>
import ArticleEditForm from "/@/components/boards/ArticleEditForm.vue";

export default {
  name: "NoticeDetail",
  components:ArticleEditForm
}
</script>
<script setup>

import {computed, inject, ref} from "vue";
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
 * 로그인된 회원 아이디
 */
const store = useStore();
const userLoggedIn = computed(()=>{
  return store.getters.getUser;
})

/**
 * 1: 상세화면, 0: 수정화면
 */
const isArticleDetail = ref(1);


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