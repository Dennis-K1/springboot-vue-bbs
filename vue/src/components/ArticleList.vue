<template>
  <main class="container mb-5">
    <div class="table-responsive">
      <table class="table text-center table-borderless">
        <thead>
        <tr class="font-weight-bold text-primary">
          <th>작성자</th>
          <th>제목</th>
          <th>이미지</th>
          <th>조회수</th>
          <th>등록일</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="article in articleList" :key="article.id">
          <td>{{ article.user.account }}</td>
          <td>
            <a href="#">{{ article.title }}</a>
          </td>
          <td>{{ article.fileAttached }}</td>
          <td>{{ article.views }}</td>
          <td>{{ article.dateRegistered }}</td>
        </tr>
        </tbody>
      </table>
      <div class="d-flex justify-content-end">
        <button class="btn btn-sm btn-primary" type="button">
          게시글 등록
        </button>
      </div>
    </div>
  </main>
</template>
<script>
export default {
  name: "Home"
}
</script>
<script setup>
import {inject} from "vue";

const articleList = inject('articleList');

</script>
<style scoped>
@media screen and (max-width: 767px) {
  .table thead th {
    font-size: 0.8rem;
  }
}

table {
  border-collapse: separate;
  border-spacing: 0 1em;
}

.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
  line-height: 1.5;
  border-radius: 0.2rem;
}
</style>