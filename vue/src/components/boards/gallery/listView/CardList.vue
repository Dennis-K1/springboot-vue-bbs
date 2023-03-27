<template>
  <div class="row row-cols-1 row-cols-md-2 g-4">
      <div  v-for="article in articleList" class="col">
        <div class="card">
          <router-link :to="`/${boardPath}/${article.id}`">
            <img :src="'data:image/jpg;base64,'+article.image"
                 class="card-img-top m-auto pt-3 ps-3"
                 alt="...">
          </router-link>
          <div class="card-body">
            <h5 class="card-title">
                <router-link :to="`/${boardPath}/${article.id}`">
                  {{ article.title }}
                </router-link>
            </h5>
            <p class="card-text">{{article.content}}</p>
          </div>
          <div class="card-footer">
            <small class="text-muted">{{article.dateRegistered}}</small>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
export default {
  name: "CardList"
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
.card-img-top {
  width: 95%;
  height: 30vw;
  object-fit: cover;
}
</style>