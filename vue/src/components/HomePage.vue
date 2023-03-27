<template>
  <main class="container">
    <div class="p-4 p-md-5 mb-4 text-white bg-primary mt-5">
      <div class="col-md-6 px-0">
        <h1 class="display-6 fst-italic">Backend : Spring Boot</h1>
        <h1 class="display-6 fst-italic">Frontend : Vue.js</h1>
      </div>
    </div>
    <div class="row mt-5 mb-2">
      <div class="mb-1 text-muted">최근 게시글 목록</div>
      <div v-for="(articles, index) in recentArticles" :key="Object.keys(recentArticles)[index]"
           class="col-md-6 mb-5">
        <div class="card h-100 shadow-sm border">
          <div class="card-body">
            <strong class="d-inline-block mb-2 text-primary p-2">{{ boardNames[index] }}</strong>
            <div class="ms-2" v-for="article in articles" :key="article.id">
              <router-link :to="'/' + boardPath[index] + '/' + article.id"><h5 class="mb-0">
                {{ article.title }}</h5>
              </router-link>
              <div class="mb-1 text-muted">{{ article.dateRegistered }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
export default {
  name: "HomePage"
}
</script>
<script setup>


import apiClient from '/@/modules/apiUtil.js'
import {computed, onMounted, reactive, ref, watchEffect} from "vue";

/**
 * 최신 게시글 목록
 */
const recentArticles = ref({});

/**
 * 게시판명
 */
const boardNames = {
  1: "공지사항",
  2: "자유게시판",
  3: "1:1문의",
  4: "갤러리"
}

/**
 * 게시판 경로명
 */
const boardPath = {
  1: "notice",
  2: "community",
  3: "inquiry",
  4: "gallery"
}

/**
 * 데이터 조회
 */
async function fetchData() {
  const indexList = await apiClient.get('index');

  /**
   * 받아온 게시글을 게시판 번호에 맞추어 재구성
   */
  let top3RecentArticlesByEachBoard = indexList.data.top3RecentArticlesByEachBoard;
  recentArticles.value = top3RecentArticlesByEachBoard.reduce((acc, obj) => {
    const {boardId, ...rest} = obj;
    if (!acc[boardId]) {
      acc[boardId] = [];
    }
    acc[boardId].push(rest);
    return acc;
  }, {})
}

onMounted(() => {
  fetchData();
});
</script>


<style scoped>
h1, h2, h3, h4, h5, h6 {
  font-family: "Playfair Display", Georgia, "Times New Roman", serif /*rtl:Amiri, Georgia, "Times New Roman", serif*/;
}

.display-4 {
  font-size: 2.5rem;
}

@media (min-width: 768px) {
  .display-4 {
    font-size: 3rem;
  }
}
a {
  text-decoration: none;
}
</style>