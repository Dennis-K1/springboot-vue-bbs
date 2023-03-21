import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

const __dirname = path.resolve();

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: {
      '/@': path.resolve(__dirname, './src'),
      '/@/components': path.resolve(__dirname, './src/components'),
      '/@/compositions': path.resolve(__dirname, './src/compositions'),
      '/@/modules': path.resolve(__dirname, './src/modules'),
      '/@/router': path.resolve(__dirname, './src/router'),
    }
  },
  plugins: [vue()],
})


