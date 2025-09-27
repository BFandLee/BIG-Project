import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // 기존 API 프록시 설정
      "/api": "http://localhost:8080",

      // ✨ 새로 추가: 이미지 경로(/uploads) 프록시 설정
      "/uploads": "http://localhost:8080",
    },
  },
});
