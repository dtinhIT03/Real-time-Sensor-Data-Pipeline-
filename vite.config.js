import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  define: {
    global: 'window' // Đảm bảo global được định nghĩa là window trong môi trường frontend
  }
})
