<template>
  <img alt="Vue logo" src="./assets/logo.png" />
</template>

<script>
import apiClient from './modules/axios'

export default {
  async mounted() {
    const data = await apiClient.get('notice');
    console.log(data);
  },
  name: 'App',
  components: {
  }
}
</script>
