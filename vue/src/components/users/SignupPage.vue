<template>
  <main class="container d-flex justify-content-center align-items-center vh-100">
    <div class="row g-3">
      <div class="col-12">
        <label for="account" class="form-label">아이디 :</label>
        <div class="form-floating">
          <input v-model="account" type="text" class="form-control" autocomplete="off">
          <button @click="checkAccountAvailability" class="btn btn-danger d-flex float-end mt-2">아이디 중복확인</button>
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
        <label for="password" class="form-label">비밀번호 확인:</label>
        <div class="form-floating">
          <input v-model="secondPassword" type="password" class="form-control" autocomplete="off">
          <span v-if="validationErrorMessage.password"
                class="d-flex text-danger">{{ validationErrorMessage.password }}</span>
        </div>
      </div>
      <div class="col-12">
        <button @click="register" type="button" class="btn btn-primary w-100">회원가입</button>
      </div>
    </div>
  </main>
</template>

<script>
export default {
  name: "RegisterPage"
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
 * 확인용 비밀번호 입력값
 */
const secondPassword = ref('');

/**
 * 유효성 실패시 발생시킬 에러 메세지
 */
const validationErrorMessage = ref({
  account: '',
  password: '',
});

/**
 * 아이디 중복확인 결과
 */
const isAccountAvailable = ref(false);

/**
 * 유효성 검증 후 회원가입
 *
 * 성공시 로그인 페이지 전환
 */
const register = async () => {
  validationErrorMessage.value.account = validation.validateAccount(account.value);
  validationErrorMessage.value.password = validation.validatePassword(password.value, secondPassword.value)
  if (validationErrorMessage.value.account !== '' || validationErrorMessage.value.password !== '') {
    return;
  }
  if (isAccountAvailable.value !== true) {
    validationErrorMessage.value.account = '아이디 중복확인을 해주세요';
    return;
  }
  let response = await apiClient.post('users',
      {account: account.value, password: password.value});
  if (response.success) {
    alert('성공적으로 가입되었습니다, 로그인 후 이용해주세요.')
    location.replace('/login');
  } else {
    alert(response.data);
  }
}

/**
 * 아이디 중복 검사
 *
 * 성공시 isAccountAvailable 값을 변경하면서 register 부분에서 정상 진행
 * 실패시 에러 응답 메세지 표시
 */
const checkAccountAvailability = async () => {
  validationErrorMessage.value.account = validation.validateAccount(account.value)
  if (validationErrorMessage.value.account !== '') {
    return;
  }
  const formData = new FormData();
  formData.append('account', account.value);
  let response = await apiClient.post('users/account-availability',
      formData);
  if (await response.success) {
    isAccountAvailable.value = true;
    validationErrorMessage.value.account = '';
  } else {
    isAccountAvailable.value = false;
    validationErrorMessage.value.account = response.error.message;
  }
}
</script>
<style scoped>

</style>