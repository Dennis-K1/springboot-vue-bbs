import apiClient from "/@/modules/apiUtil.js";
import {ref} from "vue";
import {useRouter} from "vue-router";
import {useStore} from "vuex";
import {useValidation} from "/@/compositions/useValidation.js";

/**
 * 유저 관련 composable
 */
export function useUser() {

  /**
   * 유효성 검증용 로직 및 메세지
   */
  const {userValidationErrorMessage, validateAccount, validatePassword} = useValidation();

  /**
   * 라우터
   */
  const router = useRouter();

  /**
   * Vuex 스토어
   */
  const store = useStore();

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
   * 회원 정보
   */
  const user = ref({});

  /**
   * 회원 작성 게시글 목록
   */
  const userArticleList = ref([]);

  /**
   * 아이디 중복확인 결과
   */
  const isAccountAvailable = ref(false);

  /**
   * 로그인
   * @param redirectURL 로그인 버튼 클릭 시점 경로
   */
  const login = async (redirectURL) => {
    validateAccount(account.value)
    validatePassword(password.value)
    if (userValidationErrorMessage.value.account !== '' || userValidationErrorMessage.value.password !== '') {
      return;
    }
    try {
      let response = await apiClient.post('users/login',
          {account: account.value, password: password.value});
      if (response.status === 200) {
        await store.dispatch('login',
            {userAccount: account.value, token: response.headers.authorization})
        await router.replace(redirectURL);
      }
    } catch (error) {
      console.log(error)
    }
  }

  /**
   * 로그아웃
   */
  const logout = async () => {
    await store.dispatch('logout')
    await router.go(0);
  }

  /**
   * 회원 정보 조회
   */
  const getProfile = async () => {
    const response = await apiClient.get('users/profile');
    userArticleList.value = response.data.data.ArticleList;
    user.value = response.data.data.user;
  }

  /**
   * 아이디 중복 검사
   */
  const checkAccountAvailability = async () => {
    try {
      validateAccount(account.value)
      if (userValidationErrorMessage.value.account !== '') {
        return;
      }
      const formData = new FormData();
      formData.append('account', account.value);
      const response = await apiClient.post('users/account-availability',
          formData);
      if (response.status === 200) {
        isAccountAvailable.value = true;
        userValidationErrorMessage.value.account = '';
      }
    } catch (error) {
      isAccountAvailable.value = false;
      userValidationErrorMessage.value.account = error.response.data.message;
    }
  }

  /**
   * 유효성 검증 후 회원가입
   *
   * 성공시 로그인 페이지 전환
   */
  const register = async () => {
    validateAccount(account.value)
    validatePassword(password.value, secondPassword.value)
    if (userValidationErrorMessage.value.account !== '' || userValidationErrorMessage.value.password !== '') {
      return;
    }
    if (isAccountAvailable.value !== true) {
      userValidationErrorMessage.value.account = '아이디 중복확인을 해주세요';
      return;
    }
    try {
      const response = await apiClient.post('users',
          {account: account.value, password: password.value});
      if (response.status === 200) {
        alert('성공적으로 가입되었습니다, 로그인 후 이용해주세요.')
        await router.replace('/login');
      }
    } catch (error) {
      alert(error.response.data.message);
    }
  }


  return {
    account,
    password,
    secondPassword,
    user,
    userArticleList,
    isAccountAvailable,
    userValidationErrorMessage,
    login,
    logout,
    getProfile,
    checkAccountAvailability,
    register
  }
}