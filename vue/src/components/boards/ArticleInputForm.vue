<template>
  <main class="container">
    <div class="card bg-white p-4 mt-3">
      <table class="mt-3 table">
        <tr>
          <th class="font-weight-bold text-primary" style="width: 15%;">
            제목
          </th>
          <td class="w-25"><input class="form-control mb-2" type="text"
                                  name="title" id="title"
                                  autocomplete="off"
                                  v-model="articleInput.title">
            <span v-if="articleValidationErrorMessage.title"
                  class="d-flex text-danger">{{ articleValidationErrorMessage.title }}</span>
          </td>
        </tr>
        <tr>
          <th class="font-weight-bold text-primary" style="width: 15%;">
            내용
          </th>
          <td class="w-25"><textarea class="form-control mb-2" name="content"
                                     id="content"
                                     autocomplete="off"
                                     v-model="articleInput.content"></textarea>
            <span v-if="articleValidationErrorMessage.content !== ''"
                  class="d-flex text-danger">{{ articleValidationErrorMessage.content }}</span>
          </td>
        </tr>
        <tr>
          <th class="font-weight-bold text-primary" style="width: 15%;">
            이미지 업로드
          </th>
          <td class="w-25"><input @change="addImage" class="form-control" type="file"
                                  name="file" id="file"
                                  accept=".jpg, .jpeg, .png">
            <span v-if="fileValidationErrorMessage.file !== ''"
                  class="d-flex text-danger">{{ fileValidationErrorMessage.file }}</span></td>
          <td class="w-25"><img :src="imagePreview" style="max-width: 200px; max-height: 200px">
          </td>
        </tr>
      </table>
    </div>
    <router-link :to="'/' + boardPath">
      <button class="d-flex float-start btn btn-primary mt-3">목록</button>
    </router-link>
    <button @click="registerArticle(boardPath, image)" type="button"
            class="d-flex float-end btn btn-primary mt-3">등록
    </button>
  </main>
</template>

<script>
export default {
  name: "InputForm"
}
</script>
<script setup>

import {useRoute} from "vue-router";
import {useBoard} from "/@/compositions/useBoard.js";
import {useFile} from "/@/compositions/useFile.js";

const route = useRoute();

/**
 *   registerArticle: 게시글 등록
 *   articleInput: 게시글 입력값
 *   articleValidationErrorMessage: 게시글 유효성 검증 메세지
 */
const {
  registerArticle,
  articleInput,
  articleValidationErrorMessage
} = useBoard();

/**
 *   image: 이미지
 *   imagePreview: 기존 이미지
 *   addImage: 이미지 추가
 *   fileValidationErrorMessage: 파일 유효성 검증 메세지
 */
const {
  image,
  imagePreview,
  addImage,
  fileValidationErrorMessage
} = useFile();

/**
 * 게시판 경로
 */
const boardPath = route.path.split('/')[1];

</script>

<style scoped>

</style>