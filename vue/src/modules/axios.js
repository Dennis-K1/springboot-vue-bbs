import {axios} from "@bundled-es-modules/axios";

const baseURL = 'http://localhost:8080/api/v1';
const apiClient = axios.create({
  baseURL,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
});

export default {

  // GET request
  async get(endpoint) {
    try {
      const response = await apiClient.get(endpoint);
      return response.data;
    } catch (error) {
      return error.response.data;
    }
  },

  // POST request
  async post(endpoint, payload) {
    try {
      const response = await apiClient.post(endpoint, payload);
      return response.data;
    } catch (error) {
      return error.response.data;
    }
  },

  // DELETE request
  async delete(endpoint) {
    try {
      const response = await apiClient.delete(endpoint);
      return response.data;
    } catch (error) {
      return error.response.data;
    }
  },

  // PUT request
  async put(endpoint, payload) {
    try {
      const response = await apiClient.put(endpoint, payload);
      return response.data;
    } catch (error) {
      return error.response.data;
    }
  },
}