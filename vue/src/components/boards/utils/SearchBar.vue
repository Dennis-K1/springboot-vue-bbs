<template>
  <div class="mt-5 d-flex justify-content-center">
    <span class="mt-2">기간:</span>
    <input
        class="form-control ms-2" style="width: 10%;" type="date" id="startDate"
           v-model="searchParameters.startDate"
           :max="searchParameters.endDate">

    <span class="mt-2 ms-2">~</span>
    <input class="form-control ms-2" style="width: 10%;" type="date" id="endDate"
           v-model="searchParameters.endDate"
           :min="searchParameters.startDate">

    <select class="form-select form-select-sm ms-2" style="width: 10%"
            aria-label=".form-select-sm example"
            v-model="searchParameters.searchCategory">
      <option value="1">모두 선택</option>
      <option value="2">제목</option>
      <option value="3">작성자</option>
      <option value="4">내용</option>
    </select>
    <input class="form-control w-25 ms-2" type="text" id="searchKeyword"
           v-model="searchParameters.searchKeyword"
           placeholder="검색어를 입력하세요">
    <button class="btn btn-primary ms-2" style="width: 10%" @click="search">검색</button>
  </div>
</template>

<script>
export default {
  name: "SearchBar"
}
</script>
<script setup>
import {inject} from "vue";
import {router} from "/@/router/router.js";

/**
 * BoardContainer 에서 주입받은 검색값
 * v-model 로 반영
 */
const searchParameters = inject('searchParameters');

/**
 * 페이지 이동 버튼 클릭되면
 * 1. 쿼리스트링에 검색값 반영
 * 2. event emit, BoardContainer 에서 처리
 */
const emit = defineEmits(['search']);
const search = () => {
  router.push({query: searchParameters.value});
  emit('search', searchParameters);
};
</script>

<style scoped>

</style>