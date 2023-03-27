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
          <span v-if="validationErrorMessage.reply"
                class="d-flex text-danger">{{ validationErrorMessage.reply }}</span>
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
          <div class="d-flex">
            <button class="btn btn-primary btn-sm d-flex mb-2"
                    @click="clickedNestedReplyButtonIndex = reply.id, nestedReplyContent='', validationErrorMessage.nestedReply=''">
              대댓글 등록
            </button>
            <button class="ms-2 btn btn-danger btn-sm mb-2 d-flex" @click="deleteReply(reply.id)"
                    v-if="reply.user.account === userLoggedIn">
              삭제
            </button>
          </div>
          <div class="position-relative" v-if="clickedNestedReplyButtonIndex === reply.id">
            <textarea v-model="nestedReplyContent" class="form-control pb-5 mb-3"></textarea>
            <div class="position-absolute bottom-0 end-0">
              <span v-if="validationErrorMessage.nestedReply"
                    class="text-danger me-2">{{ validationErrorMessage.nestedReply }}</span>
              <button @click="clickedNestedReplyButtonIndex = -1, nestedReplyContent=''"
                      class="btn btn-secondary mt-2 me-2 mb-1">취소
              </button>
              <button @click="registerNestedReply(reply.id, replyIndex)"
                      class="btn btn-primary mt-2 me-2 mb-1">등록
              </button>
            </div>
          </div>
        </div>
        <div v-else>
          <p>(삭제된 댓글)
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
              <button class="ms-2 btn btn-danger btn-sm mb-2 d-flex" @click="deleteNestedReply(nestedReply.id, replyIndex)"
                      v-if="reply.user.account === userLoggedIn">
                삭제
              </button>
            </div>
            <div v-else class="ms-1">
              <p>(삭제된 댓글)
                <del>{{ nestedReply.content }}</del>
              </p>
            </div>
          </div>
        </div>
      </div>
      <tr>
        <td class="reply">
          <textarea v-model="replyContent" class="form-control" rows="5"></textarea>
          <span v-if="validationErrorMessage.reply"
                class="d-flex text-danger">{{ validationErrorMessage.reply }}</span>
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
import validation from "/@/components/commons/validation.js";

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
 * 로그인된 회원 아이디
 */
const userLoggedIn = localStorage.getItem('userLoggedIn');

/**
 * ArticleDetail 에서 주입 받은 게시판 경로
 */
const boardPath = inject('boardPath');
/**
 * 댓글 등록
 *
 * 1. 유효성 검사
 * 2. 등록 성공시 서버에서 댓글 객체 반환
 * 3. 댓글 목록에 추가하여 반영
 */
const registerReply = async () => {
  validation.authenticate('reply', true);
  validationErrorMessage.value.reply = validation.validateReply(replyContent.value)
  if (validationErrorMessage.value.reply !== '') {
    return;
  }

  let response = await apiClient.post('reply',
      {articleId: articleId.value, content: replyContent.value, user: {account: userLoggedIn}});
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
  validation.authenticate('reply', true);
  validationErrorMessage.value.nestedReply = validation.validateReply(nestedReplyContent.value)
  if (validationErrorMessage.value.nestedReply !== '') {
    return;
  }
  let response = await apiClient.post('reply', {
    articleId: articleId.value,
    content: nestedReplyContent.value,
    replyId,
    user: {account: userLoggedIn}
  });
  if (response.success) {
    nestedReplyContent.value = '';
    replyList.value[replyIndex].nestedReplyList.push(response.data);
  }
}

/**
 * 유횩성 검증 에러 메세지
 */
const validationErrorMessage = ref({
  reply: '',
  nestedReply: '',

})
/**
 * 대댓글 textarea 표시를 위한 플래그
 */
let clickedNestedReplyButtonIndex = ref(-1);

/**
 * 댓글 삭제
 *
 * @param replyId 댓글 번호
 */
const deleteReply = async (replyId) => {
  let response = await apiClient.delete(`reply/${replyId}`);
  if (response.success) {

    replyList.value.forEach(reply => {
      if (reply.id === replyId) {
        reply.replyDeleted = 1;
      }
    })
  }
}

/**
 * 대댓글 삭제
 *
 * @param nestedReplyId 대댓글 번호
 * @param replyIndex 대댓글이 담긴 댓글 객체 인덱스
 */
const deleteNestedReply = async (nestedReplyId, replyIndex) => {
  let response = await apiClient.delete(`reply/${nestedReplyId}`);
  if (response.success) {
    let nestedReplyList = replyList.value[replyIndex].nestedReplyList;
    nestedReplyList.forEach(reply => {
      if (reply.id === nestedReplyId) {
        reply.replyDeleted = 1;
      }
    })
  }
}

</script>
<style scoped>

</style>