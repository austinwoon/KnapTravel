<template>
  <div>

  </div>
</template>

<script>
export default {
  data() {
    return {
      results: [
        {
          day: 1,
          destinations: [
            {
              name: "Asakusa",
              lat: 130.9,
              lng: -1.0,
            },
            {
              name: "Shinjuku",
              lat: 130.9,
              lng: -1.0,
            },
            {
              name: "Pepe",
              lat: 130.9,
              lng: -1.0,
            },
            {
              name: "Asakusa",
              lat: 130.9,
              lng: -1.0,
            },
            {
              name: "Asakusa",
              lat: 130.9,
              lng: -1.0,
            },
          ]
        },
        {
          day: 2,
          destinations: [
            {
              name: "Asakusa",
              lat: 150.9,
              lng: -3.0,
            }
          ]
        },

      ]
    }
  },
  name: "results",
  mounted: {
    // TODO(Austin): call api here w form state
  },
  computed: {
    formData() {
      return this.$store.state.form;
    }
  }
}
</script>

<style scoped>

</style>