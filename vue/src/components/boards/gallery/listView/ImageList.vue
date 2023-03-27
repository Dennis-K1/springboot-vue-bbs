<template>
  <div v-for="article in articleList" class="card p-2 mb-2">
    <div class="row g-0">
      <div class="col-md-4">
        <router-link :to="`/${boardPath}/${article.id}`">
          <img :src="'data:image/jpg;base64,'+article.image"
               class="card-img-top m-auto pt-3 ps-3"
               alt="...">
        </router-link>
      </div>
      <div class="col-md-6">
        <div class="card-body">
          <h5 class="card-title">
            <router-link :to="`/${boardPath}/${article.id}`">
              {{ article.title }}
            </router-link>
          </h5>
          <p class="card-text">{{ article.content }}</p>
          <p class="card-text"><small class="text-muted">{{ article.dateRegistered }}</small>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ImageList"
}
</script>
<script setup>

import {inject} from "vue";

/**
 * ArticleList 에서 주입 받는 게시글 목록
 */
const articleList = inject('articleList');

/**
 * ArticleList 에서 주입 받는 경로명
 */
const boardPath = inject("boardPath");

</script>

<style scoped>

</style>