import { createApp } from 'vue'
import App from './App.vue'
import './index.css'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.esm.min'
import {router} from "/@/router/router.js";

const app = createApp(App);
app.use(router);
app.mount('#app');
