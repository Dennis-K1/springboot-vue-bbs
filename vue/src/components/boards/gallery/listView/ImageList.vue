<template>
  <div v-for="article in articleList" class="card p-2 mb-2">
    <div class="row g-0">
      <div class="col-md-4">
        <a href="#">
          <img :src="'data:image/jpg;base64,'+article.image"
               class="rounded-start img-fluid mx-auto"
               alt="...">
        </a>
      </div>
      <div class="col-md-6">
        <div class="card-body">
          <h5 class="card-title">
            <a href="#">
              {{article.title}}
            </a>
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

const articleList = inject("articleList");

</script>

<style scoped>

</style>