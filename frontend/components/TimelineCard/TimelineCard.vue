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
      
      <p class="poi-desc">
        {{ pointOfInterest.description }}
      </p>
      
      <CardDetailRow v-for="detail in details"
                     :detail="pointOfInterest[detail.key]"
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
        props: {
            pointOfInterest: Object,
            visitSequence: Number,
        }
    }
</script>

<style scoped>
.poi-desc {
  text-align: left;
}
</style>