import {ref} from "vue";
import apiClient from "/@/modules/apiUtil.js";
import {useRouter} from "vue-router";
import {useValidation} from "/@/compositions/useValidation.js";

/**
 * 게시글 관련 composable
 */
export function useBoard() {

  const router = useRouter();

  /**
   * 유효성 검증용 로직 및 메세지
   */
  const {
    articleValidationErrorMessage,
    replyValidationErrorMessage,
    validateTitle,
    validateContent,
    validateReply,
    validateNestedReply,
    authenticate,
  } = useValidation();

  /**
   * 게시글 목록
   */
  const articleList = ref({});

  /**
   * 페지네이션 관련 요소
   */
  const pageParameters = ref({});

  /**
   * 게시글
   */
  const article = ref({});

  /**
   * 회원 정보
   */
  const user = ref({});

  /**
   * 댓글 목록
   */
  const replyList = ref([]);

  /**
   * 최신 게시글 목록
   */
  const recentArticleList = ref({});

  const articleInput = ref({
    title: '',
    content: '',
  })

  /**
   * 홈페이지 데이터 조회 및 게시판 번호에 맞추어 재구성
   */
  const getIndex = async () => {
    try {
      const response = await apiClient.get('index');
      let top3RecentArticlesByEachBoard = response.data.data.top3RecentArticlesByEachBoard;
      recentArticleList.value = top3RecentArticlesByEachBoard.reduce(
          (acc, obj) => {
            const {boardId, ...rest} = obj;
            if (!acc[boardId]) {
              acc[boardId] = [];
            }
            acc[boardId].push(rest);
            return acc;
          }, {})

    } catch (error) {
      alert('서버에 문제가 발생했습니다.');
    }
  }

  /**
   * 게시글 목록 조회
   *
   * @param boardPath 게시판 경로
   * @param searchParameter 검색값
   */
  const getArticleList = async (boardPath, searchParameter) => {
    try {
      const response = await apiClient.get(boardPath,
          {params: searchParameter});
      articleList.value = response.data.data.articleList;
      pageParameters.value = response.data.data.pageParameters;
    } catch (error) {
      console.log(error);
    }
  }

  /**
   * 게시판 상세 조회
   *
   * @param boardPath 게시판 경로
   * @param articleId 게시글 번호
   */
  const getArticle = async (boardPath, articleId) => {
    try {
      const response = await apiClient.get(`${boardPath}/${articleId}`);
      article.value = response.data.data;
      user.value = response.data.data.user;
    } catch (error) {
      alert(error.response.data.message)
      await router.go(-1);
    }
  }

/**
 * 댓글 조회
 *
 * @param articleId 대상 댓글이 적힌 게시글
 */
const getReply = async (articleId) => {
  try {
    const response = await apiClient.get(`reply/${articleId}`);
    replyList.value = response.data.data;
  } catch (error) {
    alert(error.response.data.message)
    await router.go(-1);
  }
}

/**
 * 게시글 등록
 * 성공 시 해당 게시글 상세페이지로 이동
 */
const registerArticle = async (boardPath, file) => {
  validateTitle(articleInput.value.title);
  validateContent(articleInput.value.content);

  if (articleValidationErrorMessage.value.title === ''
      && articleValidationErrorMessage.value.content === '') {
    const formData = new FormData();
    formData.append('title', articleInput.value.title);
    formData.append('content', articleInput.value.content);
    formData.append('file', file);
    try {
      const response = await apiClient.post(boardPath, formData, {
        headers: {
          'Content-Type': 'multipart/form-data; charset=utf-8'
        }
      });
      await router.replace(`/${boardPath}/${response.data.data}`)
    } catch (error) {
      alert(error.response.data.message);
    }
  }
}

/**
 * 게시글 등록
 * 성공 시 해당 게시글 상세페이지로 이동
 */
const editArticle = async (boardPath, articleEdit, file) => {
  validateTitle(articleEdit.title);
  validateContent(articleEdit.content);

  if (articleValidationErrorMessage.value.title === ''
      && articleValidationErrorMessage.value.content === '') {
    const formData = new FormData();
    formData.append('title', articleEdit.title);
    formData.append('content', articleEdit.content);

    if (articleEdit.newImage === 1) {
      formData.append('file', file);
    }

    try {
      const response = await apiClient.put(
          `/${boardPath}/${articleEdit.articleId}`,
          formData, {
            headers: {
              'Content-Type': 'multipart/form-data; charset=utf-8'
            }
          });
      await router.go(0);
    } catch (error) {
      console.log(error.response.data.message)
    }
  }
}

/**
 * 게시글 삭제 (작성자 회원에게만 보임)
 */
const deleteArticle = async (boardPath, articleId) => {
  try {
    const response = await apiClient.delete(`${boardPath}/${articleId}`);
    await router.replace(`/${boardPath}`);
  } catch (error) {
    console.log(error)
  }
}

/**
 * 댓글 등록
 *
 * @param reply 댓글 정보
 */
const inputReply = async (reply) => {
  await authenticate('reply', true);
  validateReply(reply.content)
  if (replyValidationErrorMessage.value.reply !== '') {
    return;
  }
  try {
    const response = await apiClient.post('reply', reply);
    router.go(0);
  } catch (error) {
    console.log(error)
  }
}

/**
 * 대댓글 등록
 *
 * @param nestedReply 대댓글 정보
 * @param replyId 대댓글이 달린 댓글 번호
 */
const inputNestedReply = async (nestedReply, replyId) => {
  await authenticate('reply', true);
  validateNestedReply(nestedReply.content)
  if (replyValidationErrorMessage.value.nestedReply !== '') {
    return;
  }
  try {
    nestedReply.replyId = replyId;
    const response = await apiClient.post('reply', nestedReply);
    router.go(0);
  } catch (error) {
    console.log(error)
  }
}

/**
 * 댓글 삭제
 *
 * @param replyId 댓글 번호
 */
const deleteReply = async (replyId) => {
  try {
    const response = await apiClient.delete(`reply/${replyId}`);
    router.go(0)
  } catch (error) {
    console.log(error)
  }
}

return {
  articleList,
  pageParameters,
  article,
  user,
  replyList,
  recentArticleList,
  articleInput,
  articleValidationErrorMessage,
  replyValidationErrorMessage,
  authenticate,
  getIndex,
  getArticleList,
  getArticle,
  getReply,
  registerArticle,
  deleteArticle,
  inputReply,
  inputNestedReply,
  deleteReply,
  editArticle
}
}