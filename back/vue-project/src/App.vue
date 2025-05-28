<template>
  <div class="nav">
    <div class="search">
      <router-link to="/">查询student表</router-link>
      <router-link to="/searchcourse">查询course表</router-link>
      <router-link to="/searchsc">查询sc表</router-link>
      <router-link to="/searchgrade">综合查询</router-link>
    </div>
  </div>

  <div class="content">
    <router-view></router-view>
  </div>
</template>
<script setup>
  import { RouterLink, RouterView } from "vue-router";
</script>
<style scoped>
  .nav {
    width: 100%;
    height: 60px;
    background: #ffffff;
    display: flex;
    justify-content: center; /* 居中布局 */
    align-items: center;
    padding: 0 20px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
    position: sticky;
    top: 0;
    z-index: 1000;
    gap: 1.5rem; /* 统一控制组间间距 */
  }

  .nav div {
    display: flex;
    gap: 0.6rem; /* 减小项间距 */
  }

  .nav div a {
    padding: 8px 12px;
    font-size: 0.95rem;
    color: #4a5568;
    text-decoration: none;
    border-radius: 6px;
    white-space: nowrap; /* 防止文字换行 */
    transition: all 0.2s ease;
    font-weight: 500;
    border: 1px solid transparent;
  }

  /* 合并查询/修改样式 */
  .search a::before {
    content: "🔍 ";
  }
  .revise a::before {
    content: "✏️ ";
  }

  /* 悬停效果 */
  .nav div a:hover {
    color: #2b6cb0;
    background: #ebf8ff;
    border-color: #bee3f8;
  }

  /* 激活状态 */
  .nav div a.router-link-exact-active {
    color: #2b6cb0;
    background: #ebf8ff;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
  }

  .content {
    padding: 1.5rem 20px;
    background: #f7fafc;
    min-height: calc(100vh - 60px);
  }

  @media (max-width: 1024px) {
    .nav {
      padding: 0 15px;
      gap: 1rem;
    }
    .nav div a {
      font-size: 0.9rem;
      padding: 6px 10px;
    }
  }

  @media (max-width: 768px) {
    .nav {
      height: auto;
      flex-wrap: wrap;
      padding: 8px 10px;
      gap: 0.5rem;
    }
    .nav div {
      flex-wrap: wrap;
      justify-content: center;
      gap: 0.4rem;
    }
    .nav div a {
      font-size: 0.85rem;
      padding: 5px 8px;
    }
    .content {
      padding: 1rem 10px;
    }
  }
</style>
