<template>
  <div class="row row-cols-1 row-cols-md-2 g-4">
      <div  v-for="article in articleList" class="col">
        <div class="card">
          <a href="#">
            <img :src="'data:image/jpg;base64,'+article.image"
                 class="card-img-top m-auto pt-3 ps-3"
                 alt="...">
          </a>
          <div class="card-body">
            <h5 class="card-title">
              <a href="#">
                {{article.title}}
              </a>
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

const articleList = inject("articleList");

</script>
<style scoped>
.card-img-top {
  width: 95%;
  height: 30vw;
  object-fit: cover;
}
</style>