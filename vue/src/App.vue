<template>
  <NavBar/>
  <router-view/>
  <FooterComponent/>
</template>

<script>
import apiClient from './modules/axios'
import NavBar from "/@/components/NavBar.vue";
import HomePage from "/@/components/HomePage.vue";
import FooterComponent from "/@/components/FooterComponent.vue";

export default {
  async mounted() {
    const data = await apiClient.get('notice');
    console.log(data);
  },
  name: 'App',
  components: {
    FooterComponent,
    HomePage,
    NavBar
  }
}
</script>
