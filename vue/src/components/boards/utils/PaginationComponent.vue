<template>
  <div id="pagination" class="d-flex justify-content-center">

    <button v-if="pageParameters.startPage !== searchParameters.pageNumber"
            @click="toPageOf(pageParameters.startPage)" class="pageButton">
      &lt;&lt;
    </button>
    <button v-else class="pageButton">
      &lt;&lt;
    </button>

    <button v-if="pageParameters.pageNumber !== pageParameters.startPage"
            @click="toPageOf(pageParameters.pageNumber - 1)"
            class="pageButton">
      &lt;
    </button>
    <button v-else class="pageButton">
      &lt;
    </button>

    <button v-for="page in pageParameters.displayedPageNumbers"
            :key="page"
            @click="toPageOf(page)"
            :class="{ pageButtonClicked: page === pageParameters.pageNumber, pageButton: page !== pageParameters.pageNumber }">
      {{ page }}
    </button>
    <button v-if="pageParameters.pageNumber !== pageParameters.endPage && pageParameters.pageNumber !== 1"
            @click="toPageOf(pageParameters.pageNumber + 1)"
            class="pageButton">
      &gt;
    </button>
    <button v-else class="pageButton">
      &gt;
    </button>
    <button v-if="pageParameters.endPage !== searchParameters.pageNumber && pageParameters.endPage !== 0"
            @click="toPageOf(pageParameters.endPage)" class="pageButton">
      &gt;&gt;
    </button>
    <button v-else class="pageButton">
      &gt;&gt;
    </button>
  </div>
</template>

<script>
export default {
  name: "PaginationComponent"
}
</script>
<script setup>
import {inject} from "vue";
import {router} from "/@/router/router.js";

/**
 * BoardContainer 에서 주입받은 검색값
 */
const searchParameters = inject('searchParameters');

/**
 * BoardContainer 에서 주입받은 페지네이션 관련 요소
 */
const pageParameters = inject('pageParameters');

/**
 * 페이지 이동 버튼 클릭되면
 * 1. 검색값(ref) 에 페이지 번호 반영
 * 2. 쿼리스트링에 검색값 반영
 * 3. event emit, BoardContainer 에서 처리
 */
const emit = defineEmits(['toPageOf']);
const toPageOf = (pageNumber) => {
  searchParameters.value.pageNumber = pageNumber;
  router.push({query: searchParameters.value});
  emit('toPageOf', searchParameters);
};
</script>

<style scoped>
a:link {
  color: black;
  text-decoration: none;
}

a:visited {
  color: black;
  text-decoration: none;
}

.pageButton {
  background-color: white;
  border: none;
  padding: 5px;
  cursor: pointer;

}

.pageButtonClicked {
  background-color: white;
  border: none;
  padding: 5px;
  font-weight: bold;
  color: blue;
}

.pageButton:hover {
  background-color: grey;
}
</style>