<template>
  <main class="container">
    <div class="p-4 p-md-5 mb-4 text-primary bg-light mt-5">
      <div class="col-md-6 px-0">
        <h1 class="display-6 fst-italic">Backend : Spring Boot</h1>
        <h1 class="display-6 fst-italic">Frontend : Vue.js</h1>
      </div>
    </div>
    <div class="row mt-5 mb-2">
      <div class="mb-1 text-muted">최근 게시글 목록</div>
      <div v-for="(articles, index) in recentArticleList"
           :key="Object.keys(recentArticleList)[index]"
           class="col-md-6 mb-5">
        <div class="card h-100 shadow-sm border">
          <div class="card-body">
            <router-link :to="boardPath[index]">
              <div class="text-center bg-primary text-light p-2 mb-3">
                {{ boardNames[index] }}
              </div>
            </router-link>
            <div class="d-flex justify-content-between align-items-center text-center">
              <span class="text-primary ms-2 fw-bold">제목</span>
              <span class="text-primary me-3 mb-2 fw-bold">등록일</span>
            </div>
            <div class="ms-2" v-for="article in articles" :key="article.id">
              <div class="d-flex justify-content-between align-items-center">
                <router-link :to="'/' + boardPath[index] + '/' + article.id">
                <span class="mb-0 text-muted">
                {{ article.title }}</span>
                </router-link>
                <div class="mb-1 text-muted">{{ article.dateRegistered }}</div>
              </div>
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


import {onMounted} from "vue";
import {useBoard} from "/@/compositions/useBoard.js";

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
 * recentArticleList : 게시판별 최신글 (3개) 목록
 * getIndex : 데이터 조회
 */
const {recentArticleList, getIndex} = useBoard();

onMounted(async () => {
  await getIndex();
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