<template>
  <div class="d-flex justify-content-center mt-3 mb-3 me-5">
    <button class="btn btn-sm" :class="{ 'btn-primary': viewIndex === 1, 'btn-outline-primary border-0': viewIndex !== 1 }" type="button" @click="changeView(1)">리스트뷰</button>
    <button class="btn btn-sm" :class="{ 'btn-primary': viewIndex === 2, 'btn-outline-primary border-0': viewIndex !== 2 }" type="button" @click="changeView(2)">갤러리뷰</button>
    <button class="btn btn-sm" :class="{ 'btn-primary': viewIndex === 3, 'btn-outline-primary border-0': viewIndex !== 3 }" type="button" @click="changeView(3)">웹진뷰</button>
  </div>
  <div v-if="viewIndex === 1">
    <SimpleList/>
  </div>
  <div v-else-if="viewIndex === 2">
    <ImageList/>
  </div>
  <div v-else-if="viewIndex === 3">
    <CardList/>
  </div>
</template>
<script>
export default {
  name: "GalleryList"
}
</script>
<script setup>
import {ref} from "vue";
import SimpleList from "/@/components/boards/gallery/listView/SimpleList.vue";
import ImageList from "/@/components/boards/gallery/listView/ImageList.vue";
import CardList from "/@/components/boards/gallery/listView/CardList.vue";

const viewIndex = ref(1);
const changeView = (index) => {
  viewIndex.value = index;
}

</script>
<style scoped>

</style>