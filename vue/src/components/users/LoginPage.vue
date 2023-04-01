<template>
  <main class="container d-flex justify-content-center align-items-center vh-100">
    <div class="row g-3">
      <div class="col-12">
        <label for="account" class="form-label">아이디 :</label>
        <div class="form-floating">
          <input v-model="account" type="text" class="form-control" autocomplete="off">
          <span v-if="userValidationErrorMessage.account"
                class="d-flex text-danger">{{ userValidationErrorMessage.account }}</span>
        </div>
      </div>
      <div class="col-12">
        <label for="password" class="form-label">비밀번호 :</label>
        <div class="form-floating">
          <input v-model="password" type="password" class="form-control" autocomplete="off">
          <span v-if="userValidationErrorMessage.password"
                class="d-flex text-danger">{{ userValidationErrorMessage.password }}</span>
        </div>
      </div>
      <div class="col-12">
        <button @click="login(redirectURL)" type="button" class="btn btn-primary w-100">로그인</button>
      </div>
    </div>
  </main>
</template>

<script>
export default {
  name: "LoginPage"
}
</script>
<script setup>

import {useRoute} from "vue-router";
import {useUser} from "/@/compositions/useUser.js";

/**
 *   login: 로그인 펑션
 *   account: 아이디
 *   password: 비밀번호
 *   userValidationErrorMessage: 유효성 검증 메세지
 */
const {
  login,
  account,
  password,
  userValidationErrorMessage
} = useUser();

/**
 * 라우팅 관련
 */
const route = useRoute();

/**
 * 회원이 특정 화면에 진입하려다 로그인 때문에 막혔을 경우 로그인이 되면 해당 지점으로 이동
 */
const redirectURL = route.query.redirectURL ? route.query.redirectURL : '/';

</script>
<style scoped>

</style>