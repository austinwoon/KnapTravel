<template>
  <div class="container">
    
    <img
        class="cover-img"
        slot="cover"
        :src="require(`~/assets/img/${coverImg}.jpg`)"
    />
    <div class="card-title">
      <a-row>
        <span class="title">{{ title }}</span>
      </a-row>
    </div>
  </div>

</template>

<script>
    export default {
        name: "ItineraryImageCard",
        props: {
            title: {
                type: String,
                required: true
            },
            formData: {
                type: Object,
                required: true,
            },
            coverImg: {
                type: String,
                required: true,
            }
        }
    }
</script>

<style scoped>
  .container {
    position: relative;
    text-align: center;
    color: white;
    width: 350px;
    height: 250px;
    transition: all .5s;
  }
  
  .container:hover {
    cursor: pointer;
    transform: translateY(-3px);
    box-shadow: 0 10px 20px rgba(0, 0, 0, .2);
  }
  
  .cover-img {
    width: 350px;
    height: 250px;
    filter: brightness(80%);
    object-fit: cover;
  }
  
  .title {
    font-weight: bolder;
    color: white;
    font-size: 20px;
    text-transform: uppercase;
    text-shadow: 1.5px 1px #ff0000;
  }
  
  .card-title {
    position: absolute;
    bottom: 24px;
    left: 16px;
  }

</style>