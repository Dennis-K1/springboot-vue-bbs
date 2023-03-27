<template>
  <NavBar/>
  <router-view/>
  <FooterComponent/>
</template>

<script>
import NavBar from "/@/components/commons/NavBar.vue";
import HomePage from "/@/components/HomePage.vue";
import FooterComponent from "/@/components/commons/FooterComponent.vue";

export default {
  name: 'App',
  components: {
    FooterComponent,
    HomePage,
    NavBar
  }
}
</script>
<script setup>

</script>
