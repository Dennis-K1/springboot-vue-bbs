import {axios} from "@bundled-es-modules/axios";
import store from "/@/modules/store.js";

/**
 * api 요청 주소
 */
const baseURL = 'http://34.64.151.93:8080/api/v1';

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
  const jwt = store.getters.getToken;
  if (jwt) {
    config.headers.Authorization = `Bearer ${jwt}`;
  }
  return config
});

export default apiClient;