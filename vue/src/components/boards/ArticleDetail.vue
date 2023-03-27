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
import {onMounted, provide, ref} from "vue";
import apiClient from "/@/modules/apiUtil.js";


const route = useRoute();

/**
 * 게시판 경로명
 */
const boardPath = ref(route.params.path);

/**
 * 게시글 번호
 */
const articleId = ref(route.params.id);

/**
 * 게시글
 */
const article = ref({});

/**
 * 회원 정보
 */
const user = ref({});

/**
 * 댓글 목록
 */
const replyList = ref([]);

/**
 * 로그인된 회원 아이디
 */
const userLoggedIn = localStorage.getItem('userLoggedIn');

/**
 * 로그인 회원 아이디 주입
 */
provide('userLoggedIn', userLoggedIn);

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
 * 게시글 조회
 */
async function fetchData() {
  let response = await apiClient.get(`${boardPath.value}/${articleId.value}`);
  article.value = await response.data;
  user.value = await response.data.user;
  response = await  apiClient.get(`reply/${articleId.value}`)
  replyList.value = await response.data;
}

/**
 * 마운트에 맞추어 데이터 조회
 */
onMounted( () => {
  fetchData();
});

</script>
<style scoped>

</style>