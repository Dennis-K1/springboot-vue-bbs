<template>
      <table class="mt-3 table">
        <tr>
          <th class="font-weight-bold text-primary" style="width: 15%;">
            제목
          </th>
          <td class="w-25"><input class="form-control mb-2" type="text"
                                  name="title" id="title"
                                  autocomplete="off"
                                  v-model="title">
            <span v-if="validationErrorMessage.title"
                  class="d-flex text-danger">{{ validationErrorMessage.title }}</span>
          </td>
        </tr>
        <tr>
          <th class="font-weight-bold text-primary" style="width: 15%;">
            내용
          </th>
          <td class="w-25"><textarea class="form-control mb-2" name="content"
                                     id="content"
                                     autocomplete="off"
                                     v-model="content"></textarea>
            <span v-if="validationErrorMessage.content"
                  class="d-flex text-danger">{{ validationErrorMessage.content }}</span>
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
          <td v-else class="w-25"><input @change="addImage" class="form-control" type="file"
                                  name="file" id="file"
                                  accept=".jpg, .jpeg, .png">
            <span v-if="validationErrorMessage.file"
                  class="d-flex text-danger">{{ validationErrorMessage.file }}</span></td>
          <td class="w-25"><img :src="imagePreview" style="max-width: 200px; max-height: 200px">
          </td>
        </tr>
  <button @click="editArticle" type="button" class="d-flex float-start w-auto btn btn-primary mt-3">등록
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
import {useRoute} from "vue-router";
import apiClient from "/@/modules/apiUtil.js";
import validation from "/@/components/commons/validation.js";

const route = useRoute();

/**
* ArticleDetail 에서 주입 받은 게시글 정보
*/
const article = inject('article');

/**
 * ArticleDetail 에서 주입 받은 게시판 경로
 */
const boardPath = inject('boardPath');

/**
 * 게시글 제목
 */
const title = ref(article.value.title);

/**
 * 게시글 내용
 */
const content = ref(article.value.content);

/**
 * 이미지 데이터
 */
const image = ref('');

/**
 * 기존 이미지
 */
const previousImage = ref(article.value.image);

/**
 * 이미지 미리보기
 */
const imagePreview = ref(null);

/**
 * 유효성 검사 실패시 표시될 메세지
 */
const validationErrorMessage = ref({
  title: '',
  content: '',
  file:''
});

/**
 * 이미지 업로드
 *
 * @param event 파일 업로드 이벤트
 */
const addImage = (event) => {
  const file = event.target.files[0]

  //파일을 업로드하지 않은 채로 업로드 창을 닫은 경우 데이터 삭제
  if (file === undefined) {
    image.value = null;
    imagePreview.value = null;
    return;
  }

  validationErrorMessage.value.file = validation.validateFile(file);
  if (validationErrorMessage.value.file !== '') {
    event.target.value = null;
    return;
  }

  image.value = file;

  setImagePreview(file);
}

/**
 * 이미지 미리보기 생성
 * @param file 파일
 */
const setImagePreview = (file) => {
  const reader = new FileReader()
  reader.onload = () => {
    imagePreview.value = reader.result
  }
  reader.readAsDataURL(file)
}

/**
 * 게시글 등록
 * 성공 시 해당 게시글 상세페이지로 이동
 */
const editArticle = async () => {

  validationErrorMessage.value.title = validation.validateTitle(title.value)
  validationErrorMessage.value.content = validation.validateContent(content.value)
  if (validationErrorMessage.value.title !== '' || validationErrorMessage.value.content !== '') {
    return;
  }

  const formData = new FormData();
  formData.append('title', title.value);
  formData.append('content', content.value);
  formData.append('file', image.value);

  let response = await apiClient.put(`/${boardPath.value}/${article.value.id}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data; charset=utf-8'
    }
  });
  if (response.success) {
    location.replace(`/${boardPath.value}/${response.data}`)
  }
}

</script>

<style scoped>

</style>