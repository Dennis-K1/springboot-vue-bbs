<template>
  <div class="fs-3 fw-bold mt-4 text-secondary">
    댓글 목록
  </div>
  <div class="card bg-white p-4 mt-3">
    <!--    댓글 없을 경우-->
    <table v-if="replyList.length === 0" class="mt-3 text-center">
      <tr>
        <td>등록된 댓글이 없습니다.</td>
      </tr>
      <tr>
        <td class="reply">
          <textarea v-model="replyContent" class="form-control" rows="5"></textarea>
          <span v-if="replyContentValidationAlert === 1" class="d-flex text-danger">댓글은 1글자 이상 99글자 미만으로 작성해야 합니다.</span>
          <button @click="registerReply"
                  class="btn btn-primary float-end mt-2">댓글 등록
          </button>
        </td>
      </tr>
    </table>

    <!--    댓글 있을 경우-->
    <table v-else class="mt-3 ">
      <div v-for="(reply, replyIndex) in replyList" :key="reply.id">
        <hr>
        <div>
          <span class="font-weight-bold text-primary">{{ reply.user.account }}</span>
          <span>{{ reply.dateRegistered }}</span>
        </div>
        <div v-if="reply.replyDeleted !== 1">
            <span>{{ reply.content }}</span>
            <button class="btn btn-primary btn-sm d-flex mb-2" @click="clickedNestedReplyButtonIndex = reply.id, nestedReplyContent=''"> 대댓글 등록 </button>
            <div class="position-relative" v-if="clickedNestedReplyButtonIndex === reply.id">
              <textarea v-model="nestedReplyContent" class="form-control pb-5 mb-3"></textarea>
              <div class="position-absolute bottom-0 end-0">
              <span v-if="nestedReplyContentValidationAlert === 1" class="text-danger me-5">댓글은 1글자 이상 99글자 미만으로 작성해야 합니다.</span>
                <button @click="clickedNestedReplyButtonIndex = -1, nestedReplyContent='', nestedReplyContentValidationAlert = -1"
                        class="btn btn-secondary mt-2 me-2 mb-1">취소
                </button>
                <button  @click="registerNestedReply(reply.id, replyIndex)"
                         class="btn btn-primary mt-2 me-2 mb-1">등록
                </button>
              </div>
            </div>
        </div>
        <div v-else>
          <p>(숨김 처리된 댓글)
            <del>{{ reply.content }}</del>
          </p>
        </div>
        <div v-if="reply.nestedReplyList !== undefined">
          <div v-for="nestedReply in reply.nestedReplyList" :key="nestedReply.id"
               class="ms-5 border-start border-3">
            <div class="ms-1">
              <span class="font-weight-bold text-primary">{{ nestedReply.account }}</span>
              <span>{{ nestedReply.dateRegistered }}</span>
            </div>
            <div v-if="nestedReply.replyDeleted != 1" class="ms-1">
              <p>{{ nestedReply.content }}</p>
            </div>
            <div v-else class="ms-1">
              <p>(숨김 처리된 댓글)
                <del>{{ nestedReply.content }}</del>
              </p>
            </div>
          </div>
        </div>
      </div>
      <tr>
        <td class="reply">
          <textarea v-model="replyContent" class="form-control" rows="5"></textarea>
          <span v-if="replyContentValidationAlert === 1" class="d-flex text-danger">댓글은 1글자 이상 99글자 미만으로 작성해야 합니다.</span>
          <button @click="registerReply"
                  class="btn btn-primary float-end mt-2">댓글 등록
          </button>
        </td>
      </tr>
    </table>


  </div>
</template>

<script>
export default {
  name: "CommunityReplyList",

}
</script>
<script setup>

import {inject, onMounted, ref} from "vue";
import apiClient from "/@/modules/apiUtil.js";

/**
 * ArticleDetail 에서 주입받은 댓글 목록
 */
const replyList = inject('replyList');

/**
 * ArticleDetail 에서 주입받은 게시글 번호
 */
const articleId = inject('articleId');

/**
 * 댓글 내용 v-model
 */
const replyContent = ref('');

/**
 * 대댓글 내용 v-model
 */
const nestedReplyContent = ref('');

/**
 * 댓글 등록
 *
 * 1. 유효성 검사
 * 2. 등록 성공시 서버에서 댓글 객체 반환
 * 3. 댓글 목록에 추가하여 반영
 */
const registerReply = async () => {
  if (replyContent.value === '' || replyContent.value.length < 1 || replyContent.value.length
      > 99) {
    replyContentValidationAlert.value = 1;
    return;
  }
  replyContentValidationAlert.value = -1;
  let response = await apiClient.post('reply', {articleId:articleId.value, content:replyContent.value, user:{account:'asd'}});
  if (response.success) {
    replyContent.value = '';
    replyList.value.push(response.data);
  }
}

/**
 * 댓글 등록
 *
 * 1. 유효성 검사
 * 2. 등록 성공시 서버에서 대댓글 객체 반환
 * 3. 댓글 목록의 해당 대댓글 목록에 추가하여 반영
 */
const registerNestedReply = async (replyId, replyIndex) => {
  if (nestedReplyContent.value === '' || nestedReplyContent.value.length < 1 || nestedReplyContent.value.length
      > 99) {
    nestedReplyContentValidationAlert.value = 1;
    return;
  }
  nestedReplyContentValidationAlert.value = -1;
  let response = await apiClient.post('reply', {articleId:articleId.value, content:nestedReplyContent.value, replyId, user:{account:'asd'}});
  if (response.success) {
    nestedReplyContent.value = '';
    replyList.value[replyIndex].nestedReplyList.push(response.data);
  }
}

/**
 * 대댓글 textarea 표시를 위한 플래그
 */
const clickedNestedReplyButtonIndex = ref(-1);

/**
 * 댓글 유효성 검사 경고를 위한 플래그
 */
const replyContentValidationAlert = ref(-1);

/**
 * 대댓글 유효성 검사 경고를 위한 플래그
 */
const nestedReplyContentValidationAlert = ref(-1);

</script>
<style scoped>

</style>