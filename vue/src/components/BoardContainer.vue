<template>
  <SearchBar @search="fetchData"/>
  <ArticleList/>
  <PaginationComponent @toPageOf="fetchData"/>
</template>

<script>
import SearchBar from "/@/components/SearchBar.vue";
import ArticleList from "/@/components/ArticleList.vue";
export default {
  name: "BoardContainer",
  components: {SearchBar, ArticleList}
}
</script>
<script setup>

/**
 * 로직:
 *
 * 1. route 에서 전달된 경로명 및 쿼리스트링 확인 및 데이터 조회
 * 2. 조회된 게시글 목록 ArticleList 에 주입 (데이터 ref 타입)
 * 3. 검색값 SearchBar 주입, 검색값 및 페지네이션 요소 PaginationComponent 주입
 * 4. SearchBar 및 PaginationComponent 에서 검색 버튼 클릭시 event emit
 * 5. emit event 에 맞추어 데이터 재조회
 */

import {onMounted, provide, ref, watch} from "vue";
import apiClient from "/@/modules/apiUtil.js";
import {useRoute} from "vue-router";
import PaginationComponent from "/@/components/PaginationComponent.vue";

/**
 * 게시글 목록
 */
const articleList = ref({});

/**
 * 페지네이션 관련 요소
 */
const pageParameters = ref({});

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
  searchCategory : 1,
  searchKeyword : route.query.searchKeyword,
  pageNumber : route.query.pageNumber
})

/**
 * 하위 컴포넌트 주입
 */
provide('articleList', articleList);
provide('searchParameters', searchParameters);
provide('pageParameters', pageParameters);

/**
 * 게시글 목록 및 페지네이션 요소 조회
 */
async function fetchData() {
  let response = await apiClient.get(boardPath.value, searchParameters.value);
  articleList.value = response.data.articleList;
  pageParameters.value = response.data.pageParameters;
}

/**
 * 경로 및 쿼리스트링이 변경된다면 감지하여 반영하고 데이터 조회 (브라우저 주소창 입력값)
 */
watch([() => route.params.path, () => route.query], async ([path, query]) => {
  boardPath.value = path;
  searchParameters.value = {
    startDate: query.startDate,
    endDate: query.enDate,
    searchCategory: 1,
    searchKeyword: query.searchKeyword,
    pageNumber: query.pageNumber
  }
  await fetchData();
});

/**
 * 마운트에 맞추어 데이터 조회
 */
onMounted(() => {
  fetchData();
});
</script>
<style scoped>

</style>