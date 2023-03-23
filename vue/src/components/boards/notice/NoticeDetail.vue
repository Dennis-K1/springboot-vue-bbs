<template>
  <div class="fs-3 fw-bold mt-4 text-secondary">
    {{boardPath}}
  </div>
  <div class="card bg-white p-4 mt-3">
    <table class="mt-3 table table-borderless">
      <tr>
        <th class="w-25 font-weight-bold text-primary">제목</th>
        <td>{{article.title}}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">작성자</th>
        <td>{{user.account}}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">조회수</th>
        <td>{{article.views}}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">등록일</th>
        <td>{{article.dateRegistered}}</td>
      </tr>
      <tr>
        <th class="w-25 font-weight-bold text-primary">내용</th>
        <td>{{article.content}}</td>
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
      <button class="btn btn-primary"
              @click="location.href=`${boardPath}/${article.id}`">
        수정
      </button>
      <button class="btn btn-primary ms-2"
              @click="deleteArticle(article.id)">삭제
      </button>
      <button class="btn btn-primary ms-2"
              @click="location.href=article.id">
        목록
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

const article = inject('article');
const boardPath = inject('boardPath');
const user = inject('user');

const deleteArticle = (articleId) => {
  console.log(articleId)
}
</script>
<style scoped>

</style>