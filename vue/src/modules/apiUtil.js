import {axios} from "@bundled-es-modules/axios";

/**
 * api 요청 주소
 */
const baseURL = 'http://localhost:8080/api/v1';

/**
 * 요청 axios 객체
 */
const apiClient = axios.create({
  baseURL,
});

/**
 * 요청시 헤더에 jwt 등록
 */
apiClient.interceptors.request.use(config => {
  const jwt = localStorage.getItem('jwt');
  if (jwt) {
    config.headers.Authorization = `Bearer ${jwt}`;
  }
  return config
});

export default {

  /**
   * GET 요청
   *
   * 성공 데이터 모두 커스텀 포맷 데이터 반환
   *
   * @param endpoint 대상 경로
   * @param parameter 파라미터
   * @returns {Promise<*>} 응답 데이터
   */
  async get(endpoint, parameter = null) {
    try {
      const response = await apiClient.get(endpoint, {params: parameter});
      return response.data;
    } catch (error) {
      return error.response.data;
    }
  },

  /**
   * POST 요청
   *
   * 성공 데이터 모두 커스텀 포맷 데이터 반환
   * 로그인 요청이고 성공한 경우 localStorage 에 jwt 등록
   *
   * @param endpoint 대상 경로
   * @param payload 전송 데이터
   * @returns {Promise<*>} 응답 데이터
   */
  async post(endpoint, payload) {
    try {
      const response = await apiClient.post(endpoint, payload);
      if (response.data.success && response.headers.authorization
          !== undefined) {
        const jwt = response.headers.authorization;
        localStorage.setItem('jwt', jwt);
      }
      return response.data;
    } catch (error) {
      return error.response.data;
    }
  },

  /**
   * DELETE 요청
   *
   * 성공 데이터 모두 커스텀 포맷 데이터 반환
   *
   * @param endpoint 대상 경로
   * @returns {Promise<*>} 응답 데이터
   */
  async delete(endpoint) {
    try {
      const response = await apiClient.delete(endpoint);
      return response.data;
    } catch (error) {
      return error.response.data;
    }
  },

  /**
   * PUT 요청
   *
   * 성공 데이터 모두 커스텀 포맷 데이터 반환
   *
   * @param endpoint 대상 경로
   * @param payload 전송 데이터
   * @returns {Promise<*>} 응답 데이터
   */
  async put(endpoint, payload) {
    try {
      const response = await apiClient.put(endpoint, payload);
      return response.data;
    } catch (error) {
      return error.response.data;
    }
  },
}