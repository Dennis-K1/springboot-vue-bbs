import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import CryptoJS from 'crypto-js'

/**
 * 상태 데이터
 * @type {{userAccount: null, token: null}}
 */
const state = {
  userAccount: null,
  token: null
}

const mutations = {
  /**
   * 유저 아이디 암호화하여 저장 (sessionStorage)
   * @param state state
   * @param userAccount 유저 아이디
   */
  setUser(state, userAccount) {
    const encryptedUserAccount = CryptoJS.AES.encrypt(JSON.stringify(userAccount), 'my-secret-key').toString()
    state.userAccount = encryptedUserAccount
  },

  /**
   * jwt 암호화하여 저장 (sessionStorage)
   * @param state state
   * @param token jwt
   */
  setToken(state, token) {
    const encryptedToken = CryptoJS.AES.encrypt(JSON.stringify(token), 'my-secret-key').toString()
    state.token = encryptedToken
  },

  /**
   * 유저 아이디 null 로 초기화
   */
  clearUser() {
    state.userAccount = null;
  },

  /**
   * jwt null 로 초기화
   */
  clearToken() {
    state.token = null;
  },
}

const actions = {
  /**
   * 로그인
   */
  login({ commit }, { userAccount, token }) {
    commit('setUser', userAccount)
    commit('setToken', token)
  },

  /**
   * 로그아웃
   * sessionStorage 에 있는 데이터 제거
   */
  logout({commit}){
    commit('clearUser');
    commit('clearToken');
    window.sessionStorage.removeItem('vuex');
  }
}

const getters = {

  /**
   * 유저 아이디 복화하여 반환
   * @returns userAccount ? null
   */
  getUser: (state) => {
    const userAccount = state.userAccount
    if (userAccount) {
      const bytes = CryptoJS.AES.decrypt(userAccount, 'my-secret-key')
      const decryptedUserAccount = JSON.parse(bytes.toString(CryptoJS.enc.Utf8))
      return decryptedUserAccount
    }
    return null
  },

  /**
   * jwt 복호화하여 반환
   * @returns jwt ? null
   */
  getToken: (state) => {
    const token = state.token
    if (token) {
      const bytes = CryptoJS.AES.decrypt(token, 'my-secret-key')
      const decryptedToken = JSON.parse(bytes.toString(CryptoJS.enc.Utf8))
      return decryptedToken
    }
    return null
  }
}

export default createStore({
  state,
  mutations,
  actions,
  getters,
  plugins: [createPersistedState({
    storage: window.sessionStorage,
    reducer: state => ({
      userAccount: state.userAccount,
      token: state.token
    })
  })],

})
