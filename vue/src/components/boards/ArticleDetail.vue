<template>
  <main class="container">
    <div v-if="boardPath !== 'inquiry'">
      <NoticeDetail/>
      <div v-if="boardPath === 'community'">
        <CommunityReplyList/>
      </div>
    </div>
    <div v-else-if="boardPath === 'inquiry'">
      <InquiryDetail/>
    </div>
  </main>
</template>
<script>
import NoticeDetail from "/@/components/boards/notice/NoticeDetail.vue";
import CommunityReplyList from "/@/components/boards/community/CommunityReplyList.vue";
import InquiryDetail from "/@/components/boards/inquiry/InquiryDetail.vue";

export default {
  name: "ArticleDetail",
  components: {NoticeDetail, CommunityReplyList, InquiryDetail }
}
</script>
<script setup>

import {useRoute} from "vue-router";
import {computed, onMounted, provide, ref} from "vue";
import {useBoard} from "/@/compositions/useBoard.js";
import {useStore} from "vuex";


const route = useRoute();

/**
 * article : 게시글 정보
 * user : 유저 정보
 * replyList : 게시글 댓글 목록
 * getArticle : 게시글 조회
 * getReply : 댓글 조회
 */
const {article, user, replyList, getArticle, getReply} = useBoard();

/**
 * 게시판 경로명
 */
const boardPath = ref(route.params.path);

/**
 * 게시글 번호
 */
const articleId = ref(route.params.id);


/**
 * 게시글 주입
 */
provide('article', article);

/**
 * 사용자 정보 주입
 */
provide('user', user);

/**
 * 댓글 목록 주입
 */
provide('replyList', replyList);

/**
 * 경로명 주입
 */
provide('boardPath', boardPath);

/**
 * 게시글 번호
 */
provide('articleId', articleId);

/**
 * 마운트에 맞추어 데이터 조회
 */
onMounted( async () => {
  await getArticle(boardPath.value, articleId.value);
  await getReply(articleId.value);
});

</script>
<style scoped>

</style>