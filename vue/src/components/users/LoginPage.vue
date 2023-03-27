<template>
  <main class="container d-flex justify-content-center align-items-center vh-100">
    <div class="row g-3">
      <div class="col-12">
        <label for="account" class="form-label">아이디 :</label>
        <div class="form-floating">
          <input v-model="account" type="text" class="form-control" autocomplete="off">
          <span v-if="validationErrorMessage.account"
                class="d-flex text-danger">{{ validationErrorMessage.account }}</span>
        </div>
      </div>
      <div class="col-12">
        <label for="password" class="form-label">비밀번호 :</label>
        <div class="form-floating">
          <input v-model="password" type="password" class="form-control" autocomplete="off">
          <span v-if="validationErrorMessage.password"
                class="d-flex text-danger">{{ validationErrorMessage.password }}</span>
        </div>
      </div>
      <div class="col-12">
        <button @click="login" type="button" class="btn btn-primary w-100">로그인</button>
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

import {onMounted, ref} from "vue";
import validation from "/@/components/commons/validation.js";
import apiClient from "/@/modules/apiUtil.js";
import {router} from "/@/router/router.js";
import {useRoute} from "vue-router";

/**
 * 아이디 입력값
 */
const account = ref('');

/**
 * 비밀번호 입력값
 */
const password = ref('');

/**
 * 유효성 실패시 발생시킬 에러 메세지
 */
const validationErrorMessage = ref({
  account: '',
  password: '',
});

/**
 * 라우팅 관련
 */
const route = useRoute();

/**
 * 회원이 특정 화면에 진입하려다 로그인 때문에 막혔을 경우 로그인이 되면 해당 지점으로 이동
 */
const redirectURL = route.query.redirectURL ? route.query.redirectURL : '/';
/**
 * 1. 입력값 검증 후 로그인 진행, 검증 실패시 validationErrorMessage 에 실패 항목 메세지 주입하여 화면 표시
 * 2. 로그인 성공시 apiUtil 에서 헤더에 있는 jwt localStorage 에 저장후 response.data 만 반환
 * 3. 반환 받은 값 확인 후 localStorage 로그인 아이디 저장 후 replace
 */
const login = async () => {
  validationErrorMessage.value.account = validation.validateAccount(account.value)
  validationErrorMessage.value.password = validation.validatePassword(password.value)
  if (validationErrorMessage.value.account !== '' || validationErrorMessage.value.password !== '') {
    return;
  }
  let response = await apiClient.post('users/login',
      {account: account.value, password: password.value});
  if (response.success && localStorage.getItem('jwt') !== undefined) {
    localStorage.setItem('userLoggedIn', account.value);
    location.replace(redirectURL);
  } else {
    alert('로그인 실패');
  }
}
</script>
<style scoped>

</style>