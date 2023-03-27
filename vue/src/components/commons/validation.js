import {router} from "/@/router/router.js";

const MAX_FILE_SIZE = 1024 * 1024 * 5;
const ALLOWED_EXTENSIONS = ['image/png', 'image/jpeg', 'image/jpg'];

/**
 * 유효성 검증 로직 모음
 */
export default {
  /**
   * 아이디 유효성 검증
   *
   * @param account 유저 입력 아이디
   * @returns {string} 해당하는 에러 메세지, 공백일 경우 정상
   */
  validateAccount(account) {
    if (account === '' || account.length < 3 || account.length
        > 9) {
      return '아이디를 3글자 이상 10글자 미만으로 입력해주세요.';
    } else {
      return '';
    }
  },

  /**
   * 비밀번호 유효성 검증
   *
   * @param password 유저 입력 비밀번호
   * @param secondPassword 회원가입시 입력된 비밀번호 확인용 입력값
   * @returns {string} 해당하는 에러 메세지, 공백일 경우 정상
   */
  validatePassword(password, secondPassword = null) {
    if (password === '') {
      return '비밀번호를 입력해주세요.';
    } else if (password.length < 4 || password.length > 16) {
      return "비밀번호는 특수문자( '!,@,$,%,^,&,*' 만 허용), 영문 대소문자, 숫자를 포함하여 4글자 이상 16글자 미만으로 입력해주세요.";
    } else if (!password.match(
        /([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])/)) {
      return "비밀번호는 특수문자( '!,@,$,%,^,&,*' 만 허용), 영문 대소문자, 숫자를 포함하여 4글자 이상 16글자 미만으로 입력해주세요.";
    } else if (secondPassword !== null && password !== secondPassword) {
      return '비밀번호 확인란에 동일한 비밀번호를 입력해주세요.';
    } else {
      return '';
    }
  },

  /**
   * 게시글 제목 유효성 검증
   *
   * @param title 유저 입력 제목
   * @returns {string} 해당하는 에러 메세지, 공백일 경우 정상
   */

  validateTitle(title) {
    if (title === '') {
      return '제목을 입력해주세요.';
    } else if (title.length < 4 || title.length > 100) {
      return '제목을 4글자 이상 100글자 미만으로 입력해주세요.';
    } else {
      return '';
    }
  },

  /**
   * 게시글 내용 유효성 검증
   *
   * @param content 유저 입력 내용
   * @returns {string} 해당하는 에러 메세지, 공백일 경우 정상
   */
  validateContent(content) {
    if (content === '') {
      return '내용을 입력해주세요.';
    } else if (content.length < 4 || content.length > 2000) {
      return '내용을 4글자 이상 2000글자 미만으로 입력해주세요.';
    } else {
      return '';
    }
  },

  /**
   * 댓글(답글) 내용 유효성 검증
   *
   * @param reply 유저 입력 댓글 내용
   * @returns {string} 해당하는 에러 메세지, 공백일 경우 정상
   */
  validateReply(reply) {
    if (reply === '' || reply.length < 1 || reply.length
        > 99) {
      return '내용을 1글자 이상 100글자 미만으로 입력해주세요.';
    } else {
      return '';
    }
  },

  /**
   * 파일 유효성 검증
   *
   * @param file 업로드된 파일 객체
   * @returns {string} 해당하는 에러 메세지, 공백일 경우 정상
   */
  validateFile(file) {
    if (file.size > MAX_FILE_SIZE) {
      return '파일은 5MB 이하로 업로드해주세요.';
    } else if (!ALLOWED_EXTENSIONS.includes(file.type)) {
      return '파일 확장자는 png, jpg, jpeg 만 가능합니다.';
    } else {
      return '';
    }
  },

  /**
   * 로그인이 필요한 서비스 이용전 로그인되어있는지 확인
   *
   * @param targetPath 대상 서비스 경로
   */
  authenticate(targetPath) {
    const userLoggedIn = localStorage.getItem('userLoggedIn');
    if (userLoggedIn !== null) {
      router.push(targetPath);
    } else {
      alert('로그인이 필요합니다.')
      location.replace(`/login?redirectURL=${targetPath}`);
    }
  }
}

