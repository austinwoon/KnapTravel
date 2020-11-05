<template>
  <a-menu mode="horizontal" theme="dark">
    <NuxtLink to="/">
      <span style="margin: 0 16px 0 16px; font-weight: bolder; font-size: 20px; color: aqua">KNAPTRAVEL</span>
    </NuxtLink>
    
    <a-menu-item>
      <NuxtLink to="/">
        <a-icon type="home"/>
        Home
      </NuxtLink>
    </a-menu-item>
    
    <a-menu-item>
      <NuxtLink to="/results">
      <a-icon type="eye"/>
        Discover
      </NuxtLink>
    </a-menu-item>
    
    <a-menu-item style="float: right">
      <a-avatar src="/austin_avatar.jpeg"></a-avatar>
      Hi, <span style="font-weight: bolder; font-size: 16px">Austin</span>
    </a-menu-item>
  </a-menu>
</template>

<script>
    export default {
        name: "NavBar"
    }
</script>

<style scoped>

</style>