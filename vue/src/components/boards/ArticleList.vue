<template>
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
</template>
<script>
export default {
  name: "ArticleList"
}
</script>
<script setup>
import GalleryList from "/@/components/boards/gallery/GalleryList.vue";
import CommunityList from "/@/components/boards/community/CommunityList.vue";
import InquiryList from "/@/components/boards/inquiry/InquiryList.vue";
import NoticeList from "/@/components/boards/notice/NoticeList.vue";
import {inject} from "vue";

const boardPath = inject("boardPath");

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

.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
  line-height: 1.5;
  border-radius: 0.2rem;
}
</style>