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

import {inject} from "vue";

const article = inject('article');
const boardPath = inject('boardPath');
const user = inject('user');
const replyList = inject('replyList');
console.log(replyList)

</script>
<style scoped>

</style>