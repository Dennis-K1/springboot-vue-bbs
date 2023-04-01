<template>
  <table class="mt-3 table">
    <tr>
      <th class="font-weight-bold text-primary" style="width: 15%;">
        제목
      </th>
      <td class="w-25"><input class="form-control mb-2" type="text"
                              name="title" id="title"
                              autocomplete="off"
                              v-model="articleEdit.title">
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
                                 v-model="articleEdit.content"></textarea>
        <span v-if="articleValidationErrorMessage.content"
              class="d-flex text-danger">{{ articleValidationErrorMessage.content }}</span>
      </td>
    </tr>
    <tr>
      <th class="font-weight-bold text-primary" style="width: 15%;">
        이미지
      </th>
      <td v-if="previousImage !== undefined">
        <img :src="'data:image/jpg;base64,'+previousImage"
             width="240"
             height="300"/>
        <button @click="previousImage = undefined">삭제</button>
      </td>
      <td v-else class="w-25"><input @change="addImage"
                                     @click="articleEdit.newImage = 1"
                                     class="form-control" type="file"
                                     name="file" id="file"
                                     accept=".jpg, .jpeg, .png">
        <span v-if="fileValidationErrorMessage.file"
              class="d-flex text-danger">{{ fileValidationErrorMessage.file }}</span></td>
      <td class="w-25"><img :src="imagePreview" style="max-width: 200px; max-height: 200px">
      </td>
    </tr>
    <button @click="editArticle(boardPath, articleEdit, image)" type="button"
            class="d-flex float-start w-auto btn btn-primary mt-3">등록
    </button>
  </table>
</template>

<script>
export default {
  name: "EditForm"
}
</script>
<script setup>

import {inject, ref} from "vue";
import {useRouter} from "vue-router";
import {useBoard} from "/@/compositions/useBoard.js";
import {useFile} from "/@/compositions/useFile.js";

const router = useRouter();
const {articleValidationErrorMessage, editArticle} = useBoard();
const {image, imagePreview, addImage, fileValidationErrorMessage} = useFile();

/**
 * ArticleDetail 에서 주입 받은 게시글 정보
 */
const article = inject('article');

/**
 * ArticleDetail 에서 주입 받은 게시판 경로
 */
const boardPath = inject('boardPath');

/**
 * 기존 이미지
 */
const previousImage = ref(article.value.image);

/**
 * 게시글 수정 데이터
 *
 * newImage : input 태그 클릭시 1 로 변경하여 새로운 이미지가 있거나 기존 이미지가 삭제되었음을 알림
 */
const articleEdit = ref({
  articleId : article.value.id,
  title : article.value.title,
  content : article.value.content,
  newImage : 0
})

</script>

<style scoped>

</style>