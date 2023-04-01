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
          <textarea v-model="newReply.content" class="form-control" rows="5"></textarea>
          <span v-if="replyValidationErrorMessage.reply"
                class="d-flex text-danger">{{ replyValidationErrorMessage.reply }}</span>
          <button @click="inputReply(newReply)"
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
                    @click="clickedNestedReplyButtonIndex = reply.id, newNestedReply.content='', replyValidationErrorMessage.nestedReply=''">
              대댓글 등록
            </button>
            <button class="ms-2 btn btn-danger btn-sm mb-2 d-flex" @click="deleteReply(reply.id)"
                    v-if="reply.user.account === userLoggedIn">
              삭제
            </button>
          </div>
          <div class="position-relative" v-if="clickedNestedReplyButtonIndex === reply.id">
            <textarea v-model="newNestedReply.content" class="form-control pb-5 mb-3"></textarea>
            <div class="position-absolute bottom-0 end-0">
              <span v-if="replyValidationErrorMessage.nestedReply"
                    class="text-danger me-2">{{ replyValidationErrorMessage.nestedReply }}</span>
              <button @click="clickedNestedReplyButtonIndex = -1, newNestedReply.content=''"
                      class="btn btn-secondary mt-2 me-2 mb-1">취소
              </button>
              <button @click="inputNestedReply(newNestedReply, reply.id)"
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
            <div v-if="nestedReply.replyDeleted !== 1" class="ms-1">
              <p>{{ nestedReply.content }}</p>
              <button class="ms-2 btn btn-danger btn-sm mb-2 d-flex"
                      @click="deleteReply(nestedReply.id)"
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
          <textarea v-model="newReply.content" class="form-control" rows="5"></textarea>
          <span v-if="replyValidationErrorMessage.reply"
                class="d-flex text-danger">{{ replyValidationErrorMessage.reply }}</span>
          <button @click="inputReply(newReply)"
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

import {computed, inject, onMounted, ref} from "vue";
import {useBoard} from "/@/compositions/useBoard.js";
import {useStore} from "vuex";

const {inputReply, inputNestedReply,deleteReply, replyValidationErrorMessage, authenticate} = useBoard();

/**
 * ArticleDetail 에서 주입받은 댓글 목록
 */
const replyList = inject('replyList');

/**
 * ArticleDetail 에서 주입받은 게시글 번호
 */
const articleId = inject('articleId');

/**
 * 로그인된 회원 아이디
 */
const store = useStore();
const userLoggedIn = computed(() => {
  return store.getters.getUser;
})

/**
 * ArticleDetail 에서 주입 받은 게시판 경로
 */
const boardPath = inject('boardPath');

/**
 * 댓글 입력값
 */
const newReply = ref({
  articleId: articleId.value,
  content: '',
  user:
      {
        account: userLoggedIn
      }
})

/**
 * 대댓글 입력값
 */
const newNestedReply = ref({
  articleId: articleId.value,
  content: '',
  user:
      {
        account: userLoggedIn
      }
})

/**
 * 대댓글 textarea 표시를 위한 플래그
 */
let clickedNestedReplyButtonIndex = ref(-1);


</script>
<style scoped>

</style>