<template>
  <SearchBar/>
  <main class="container">
    <div v-if="boardPath === 'notice'">
      <NoticeList/>
    </div>
    <div v-else-if="boardPath === 'community'">
      <CommunityList/>
    </div>
    <div v-else-if="boardPath === 'inquiry'">
      <InquiryList/>
    </div>
    <div v-else-if="boardPath === 'gallery'">
      <GalleryList/>
    </div>
  </main>
  <PaginationComponent/>
</template>
<script>
import PaginationComponent from "/@/components/boards/utils/PaginationComponent.vue";
import GalleryList from "/@/components/boards/gallery/GalleryList.vue";
import InquiryList from "/@/components/boards/inquiry/InquiryList.vue";
import CommunityList from "/@/components/boards/community/CommunityList.vue";
import NoticeList from "/@/components/boards/notice/NoticeList.vue";
import SearchBar from "/@/components/boards/utils/SearchBar.vue";

export default {
  name: "ArticleList",
  components:{SearchBar, NoticeList, CommunityList, InquiryList, GalleryList, PaginationComponent}
}
</script>
<script setup>

/**
 * 로직:
 *
 * 1. route 에서 전달된 경로명 및 쿼리스트링 확인 및 데이터 조회
 * 2. 조회된 게시글 목록 ArticleList 에 주입 (데이터 ref 타입)
 * 3. 검색값 SearchBar 주입, 검색값 및 페지네이션 요소 PaginationComponent 주입
 * 4. SearchBar 및 PaginationComponent 에서 검색 혹은 페이지 이동 버튼 클릭시 rotuer 로 경로 반영
 * 5. url 경로 변경시 watch 가 감지하여 데이터 재조회
 */

import {computed, onMounted, provide, ref, watch} from "vue";
import {useRoute} from "vue-router";
import {useBoard} from "/@/compositions/useBoard.js";
import {useStore} from "vuex";

/**
 * 라우팅 관련
 */
const route = useRoute();


/**
 * 게시판 경로명
 */
const boardPath = ref(route.params.path);

/**
 * 검색값
 */
const searchParameters = ref({
  startDate :route.query.startDate,
  endDate : route.query.enDate,
  searchCategory : '1',
  searchKeyword : route.query.searchKeyword,
  pageNumber : route.query.pageNumber
})

/**
 * 로그인된 회원 아이디
 */
const store = useStore();
const userLoggedIn = computed(() => {
  return store.getters.getUser
})

/**
 * articleList : 해당 경로 게시글 목록
 * pageParameters : 페이징 요소
 * getArticleList : 상기 데이터 조회
 */
const {articleList, pageParameters, getArticleList} = useBoard();

/**
 * 게시글 목록 주입
 */
provide('articleList', articleList);

/**
 * 로그인 회원 아이디 주입
 */
provide('userLoggedIn', userLoggedIn);

/**
 * 게시판명 주입
 */
provide('boardPath', boardPath);

/**
 * 검색값 주입
 */
provide('searchParameters', searchParameters);

/**
 * 페이징 요소 주입
 */
provide('pageParameters', pageParameters);


/**
 * 경로 및 쿼리스트링이 변경된다면 감지하여 반영하고 데이터 조회 (브라우저 주소창 입력값)
 */
watch([() => route.params.path, () => route.query], async ([path, query]) => {
  boardPath.value = path;
  searchParameters.value = {
    startDate: query.startDate,
    endDate: query.enDate,
    searchCategory: query.searchCategory === undefined?1:query.searchCategory,
    searchKeyword: query.searchKeyword,
    pageNumber: query.pageNumber
  }
  await getArticleList(boardPath.value, searchParameters.value);
});

/**
 * 마운트에 맞추어 데이터 조회
 */
onMounted(async () => {
    await getArticleList(boardPath.value, searchParameters.value);
});
</script>

<style scoped>
@media screen and (max-width: 767px) {
  .table thead th {
    font-size: 0.8rem;
  }
}

table {
  border-collapse: separate;
  border-spacing: 0 1em;
}
</style>