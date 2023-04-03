import {ref} from "vue";
import {useRouter} from "vue-router";
import store from "/@/modules/store.js";

/**
 * 최대 파일 사이즈
 */
const MAX_FILE_SIZE = 1024 * 1024 * 5;

/**
 * 허용 확장자
 */
const ALLOWED_EXTENSIONS = ['image/png', 'image/jpeg', 'image/jpg'];

/**
 * 유효성 검증 관련 composable
 */
export function useValidation() {

  const router = useRouter();

  /**
   * 게시글 유효성 검사 메세지
   */
  const articleValidationErrorMessage = ref({
    title: '',
    content: '',
  });

  /**
   * 파일 유효성 검사 메세지
   */
  const fileValidationErrorMessage = ref({
    file:''
  })

  /**
   * 댓글 유횩성 검증 메세지
   */
  const replyValidationErrorMessage = ref({
    reply: '',
    nestedReply: '',
  })

  /**
   * 회원 유효성 검증 메세지
   */
  const userValidationErrorMessage = ref({
    account: '',
    password: '',
  });

  /**
   * 제목 유효성 검증
   * @param title 제목 입력값
   */
  const validateTitle = (title) => {
    if (title === '') {
      articleValidationErrorMessage.value.title = '제목을 입력해주세요.';
      return;
    }
    if (title.length < 5 || title.length > 50) {
      articleValidationErrorMessage.value.title = '제목을 5글자 이상 50글자 미만으로 입력해주세요.';
      return;
    }
    articleValidationErrorMessage.value.title = '';
  }

  /**
   * 내용 유효성 검증
   * @param content 내용 입력값
   */
  const validateContent = (content) => {
    if (content === '') {
      articleValidationErrorMessage.value.content = '내용을 입력해주세요.';
      return;
    }
    if (content.length < 30 || content.length > 500) {
      articleValidationErrorMessage.value.content = '내용을 30글자 이상 500글자 미만으로 입력해주세요.';
      return;
    }
    articleValidationErrorMessage.value.content = '';
  }

  /**
   * 파일 유효성 검증
   * @param file 파일
   */
  const validateFile = (file) => {
    if (file.size > MAX_FILE_SIZE) {
      fileValidationErrorMessage.value.file = '파일은 5MB 이하로 업로드해주세요.';
      return;
    }
    if (!ALLOWED_EXTENSIONS.includes(file.type)) {
      fileValidationErrorMessage.value.file = '파일 확장자는 png, jpg, jpeg 만 가능합니다.';
      return;
    }
    fileValidationErrorMessage.value.file = '';
  }

  /**
   * 댓글 유효성 검증
   * @param reply 댓글
   */
  const validateReply = (reply) => {
    if (reply === '' || reply.length < 1 || reply.length
        > 99) {
      replyValidationErrorMessage.value.reply = '내용을 1글자 이상 100글자 미만으로 입력해주세요.';
      return;
    }
    replyValidationErrorMessage.value.reply = '';
  }

  /**
   * 대댓글 유효성 검증
   * @param nestedReply 대댓글
   */
  const validateNestedReply = (nestedReply) => {
    if (nestedReply === '' || nestedReply.length < 1 || nestedReply.length
        > 99) {
      replyValidationErrorMessage.value.nestedReply = '내용을 1글자 이상 100글자 미만으로 입력해주세요.';
      return;
    }
      replyValidationErrorMessage.value.nestedReply = '';
  }

  /**
   * 로그인이 필요한 서비스 이용전 로그인되어있는지 확인
   *
   * @param targetPath 대상 서비스 경로
   * @param isPost 단순 페이지 반환이 아닌 POST 요청인 경우
   */
  const authenticate = async (targetPath, isPost = null) => {
    const userLoggedIn = store.state.userAccount;
    if (userLoggedIn !== null) {
      if (isPost === true) {
        return;
      }
      await router.push(targetPath);
    } else {
      alert('로그인이 필요합니다.')
      await router.replace(`/login?redirectURL=${targetPath}`);
    }
  }

  /**
   * 아이디 유효성 검증
   *
   * @param account 유저 입력 아이디
   */
  const validateAccount = (account) => {
    if (account === '' || account.length < 3 || account.length
        > 9) {
      userValidationErrorMessage.value.account = '아이디를 3글자 이상 10글자 미만으로 입력해주세요.';
      return;
    }
    userValidationErrorMessage.value.account = '';
  }

  /**
   * 비밀번호 유효성 검증
   *
   * @param password 유저 입력 비밀번호
   * @param secondPassword 회원가입시 입력된 비밀번호 확인용 입력값
   */
  const validatePassword = (password, secondPassword = null) => {
    if (password === '') {
      userValidationErrorMessage.value.password =  '비밀번호를 입력해주세요.';
      return ;
    }
    if (password.length < 4 || password.length > 16) {
      userValidationErrorMessage.value.password =  "비밀번호는 특수문자( '!,@,$,%,^,&,*' 만 허용), 영문 대소문자, 숫자를 포함하여 4글자 이상 16글자 미만으로 입력해주세요.";
      return ;
    }
    if (!password.match(/^[a-zA-Z0-9!@\$%\^&\*]+$/)) {
      userValidationErrorMessage.value.password =  "비밀번호는 특수문자( '!,@,$,%,^,&,*' 만 허용), 영문 대소문자, 숫자를 포함하여 4글자 이상 16글자 미만으로 입력해주세요.";
      return ;
    }
    if (secondPassword !== null && password !== secondPassword) {
      userValidationErrorMessage.value.password =  '비밀번호 확인란에 동일한 비밀번호를 입력해주세요.';
      return ;
    }
      userValidationErrorMessage.value.password =  '';
  }

  return {
    articleValidationErrorMessage,
    replyValidationErrorMessage,
    userValidationErrorMessage,
    fileValidationErrorMessage,
    validateTitle,
    validateContent,
    validateFile,
    validateReply,
    validateNestedReply,
    authenticate,
    validateAccount,
    validatePassword
  }
}