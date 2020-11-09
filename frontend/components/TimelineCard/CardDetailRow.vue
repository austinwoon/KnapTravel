<template>
  <a-row type="flex" style="margin-top: 8px">
    <a-col style="margin-right: 8px">
      <slot name="icon"></slot>
    </a-col>
    <a-col>
      {{ detail || nullMessage }}
    </a-col>
  </a-row>
</template>

<script>
    export default {
        name: "CardDetailRow",
        props: {
            detail: String,
            nullMessage : {
                type: String,
                default: "No info found!"
            }
        }
    }
</script>

<style scoped>

</style>