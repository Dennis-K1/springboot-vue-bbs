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
          <textarea id="reply" class="form-control"></textarea>
          <button @click="null"
                  class="btn btn-primary float-end mt-2">댓글 등록
          </button>
        </td>
      </tr>
    </table>

    <!--    댓글 있을 경우-->
    <table v-else class="mt-3 ">
      <div v-for="reply in replyList" :key="reply.id">
        <hr>
        <div>
          <span class="font-weight-bold text-primary">{{ reply.user.account }}</span>
          <span>{{ reply.dateRegistered }}</span>
        </div>
        <div v-if="reply.replyDeleted !== 1">
            <span>{{ reply.content }}</span>
            <button class="btn btn-primary btn-sm d-flex mb-2" @click="clickedNestedReplyButtonIndex = reply.id"> 대댓글 등록 </button>
            <div class="position-relative" v-if="clickedNestedReplyButtonIndex === reply.id">
              <textarea class="form-control pb-5 mb-3"></textarea>
              <div class="position-absolute bottom-0 end-0">
                <button @click="clickedNestedReplyButtonIndex = -1"
                        class="btn btn-secondary mt-2 me-2">취소
                </button>
                <button  @click="null"
                         class="btn btn-primary mt-2 me-2">저장
                </button>
              </div>
            </div>
        </div>
        <div v-else>
          <p>(숨김 처리된 댓글)
            <del>{{ reply.content }}</del>
          </p>
        </div>
        <div v-if="reply.nestedReplyList.length > 0">
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
          <textarea id="reply" class="form-control" rows="5"></textarea>
          <button @click="null"
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

import {inject, ref} from "vue";

/**
 * ArticleDetail 에서 주입
 */
const replyList = inject('replyList');

/**
 * 대댓글 textarea 표시를 위한 플래그
 */
const clickedNestedReplyButtonIndex = ref(-1);

</script>
<style scoped>

</style>