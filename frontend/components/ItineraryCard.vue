<template>
  <div>
    <a-card :title="title" class="itinerary-card">
      <a-row style="margin-bottom: 16px">
        <MapPlot
            :id="title"
            :plot-points="plotPoints"
        />
      </a-row>
      
      <a-row>
        <template>
          <a-timeline
              mode="alternate"
              class="timeline-width"
          >
            <a-timeline-item
                v-for="(n, day) in this.days"
                :key="dailyItinerary[day].name"
            >
              <TimelineCard :point-of-interest="dailyItinerary[day]" :visitSequence="day"/>
            </a-timeline-item>
          </a-timeline>
        </template>
      </a-row>
    </a-card>
  </div>
</template>

<script>
    import MapPlot from "./MapPlot";
    import TimelineCard from "./TimelineCard/TimelineCard";

    export default {
        name: "ItineraryCard",
        components: {TimelineCard, MapPlot},
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
                Object.values(this.dailyItinerary).forEach(place => {
                    const {lat, lng} = place.coordinate.coordinates;
                    plotPoints.push({name: place.name, coordinates: [lat, lng]});
                });

                return plotPoints;
            }
        }
    }
</script>

<style scoped>

</style>