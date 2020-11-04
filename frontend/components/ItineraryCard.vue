<template>
  <div>
    
    <a-card :title="title">
      <a-row style="margin-bottom: 16px">
        <MapPlot
            :id="title"
            :plot-points="plotPoints"
        />
      </a-row>
      
      <a-row>
        <template>
          <a-timeline>
            <a-timeline-item v-for="day in this.days"> {{dailyItinerary[day]}}</a-timeline-item>
          </a-timeline>
        </template>
      </a-row>
    </a-card>
  </div>
</template>

<script>
    import MapPlot from "./MapPlot";

    export default {
        name: "ItineraryCard",
        components: {MapPlot},
        props: {
            "dailyItinerary": {
                type: Object,
                required: true,
            },
            "title": {
                type: String,
                required: true,
            }
        },
        computed: {
            days() {
                return Object.keys(this.dailyItinerary).length;
            },
            plotPoints() {
                const plotPoints = [];
                Object.values(this.dailyItinerary).forEach( place => {
                    const { lat, lng } = place.coordinate.coordinates;
                    plotPoints.push({ title: place.name, coordinates: [lat, lng]});
                });
                
                console.log(plotPoints)
                return plotPoints;
            }
        }
    }
</script>

<style scoped>

</style>