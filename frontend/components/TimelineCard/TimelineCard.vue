<template>
  <a-collapse
      :expand-icon-position="visitSequence % 2 == 0 ? 'left' : 'right'"
  >
    <template #expandIcon="props">
      <a-icon
          v-if="visitSequence % 2 == 0"
          type="caret-right"
          :rotate="props.isActive ? 90 : 0"
      />
      <a-icon
          v-else
          type="caret-left"
          :rotate="props.isActive ? -90 : 0"
      />
    </template>
    
    <a-collapse-panel :header="pointOfInterest.name">
      <img
          v-if="this.imgSrc"
          class="location-img"
          :src="this.imgSrc"
          alt="Photo Of Location"
      />
      
      <p class="poi-desc">
        {{ pointOfInterest.description }}
      </p>
      
      <CardDetailRow v-for="detail in details"
                     :detail="`${pointOfInterest[detail.key]}`"
                     :null-message="detail.nullMessage"
                     :key="detail.key + pointOfInterest.name"
      >
        <template v-slot:icon>
          <a-icon :type="detail.icon"/>
        </template>
      </CardDetailRow>
    
    </a-collapse-panel>
  
  </a-collapse>
</template>

<script>
    import CardDetailRow from "./CardDetailRow";

    export default {
        name: "TimelineCard",
        components: {CardDetailRow},
        data() {
            return {
                imgSrc: '',
                details: [
                    {
                        key: "openingHours",
                        nullMessage: "No Opening Hours Found!",
                        icon: "clock-circle"
                    },
                    {
                        key: "address",
                        nullMessage: "No Address Found!",
                        icon: "environment"
                    },
                    {
                        key: "price",
                        nullMessage: "No Price Found!",
                        icon: "dollar"
                    },
                    {
                        key: "hours",
                        nullMessage: "No Reccomended Visiting Hours!",
                        icon: "hourglass"
                    },

                ]
            }
        },
        async created() {
            try {
              const { results : photos } = await this.$axios
                  .$get(`https://api.unsplash.com/search/photos`, {
                      params: {
                          page: 1,
                          query: this.pointOfInterest.name,
                          w: 450,
                          h: 300,
                          dpr: 2,
                          client_id: process.env.UNSPLASH_ACCESS_KEY
                      }
                  });
              
              this.imgSrc = photos[0].urls.small;
            } catch (e) {
                // console.log("FAILED TO CALL UNSPLASH", e);
            }
        },
        props: {
            pointOfInterest: Object,
            visitSequence: Number,
        }
    }
</script>

<style scoped>
  .location-img {
    max-width: 300px;
    max-height: 250px;
    width: 90%;
    height: 90%;
    margin: 10px 0 10px 0;
    object-fit: cover;
  }
.poi-desc {
  text-align: left;
}
</style>