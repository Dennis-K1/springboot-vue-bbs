<template>
  <main class="container d-flex justify-content-center align-items-center vh-100">
    <form>
    <div class="row g-3">
      <div class="col-12">
        <label for="account" class="form-label">아이디 :</label>
        <div class="form-floating">
          <input v-model="account" type="text" class="form-control" autocomplete="off">
          <button type="button" @click="checkAccountAvailability" class="btn btn-danger d-flex float-end mt-2">아이디
            중복확인
          </button>
          <span v-if="userValidationErrorMessage.account"
                class="text-danger">{{ userValidationErrorMessage.account }}</span>
        </div>
      </div>
      <div class="col-12">
        <label for="password" class="form-label">비밀번호 :</label>
        <div class="form-floating">
          <input v-model="password" type="password" class="form-control" autocomplete="off">
          <span v-if="userValidationErrorMessage.password"
                class=" text-danger">{{ userValidationErrorMessage.password }}</span>
        </div>
      </div>
      <div class="col-12">
        <label for="password" class="form-label">비밀번호 확인:</label>
        <div class="form-floating">
          <input v-model="secondPassword" type="password" class="form-control" autocomplete="off">
          <span v-if="userValidationErrorMessage.password"
                class=" text-danger">{{ userValidationErrorMessage.password }}</span>
        </div>
      </div>
      <div class="col-12">
        <button @click="register" type="button" class="btn btn-primary w-100">회원가입</button>
      </div>
    </div>
    </form>
  </main>
</template>

<script>
export default {
  name: "RegisterPage"
}
</script>
<script setup>

import {useUser} from "/@/compositions/useUser.js";

/**
 *  account: 아이디
 *  password: 비밀번호
 *  secondPassword: 확인용 비밀번호
 *  checkAccountAvailability: 중복확인 로직
 *  register: 회원가입 로직
 *  userValidationErrorMessage: 유효성 검증 메세지
 *  */
const {
  account,
  password,
  secondPassword,
  checkAccountAvailability,
  register,
  userValidationErrorMessage
} = useUser();

</script>
<style scoped>

</style>