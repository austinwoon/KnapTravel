<template>
  <div>
    <a-card :title="title" class="itinerary-card">
      <a slot="extra">
        <download-csv
            :name="fileName"
            :data="downloadData"
            :labels="downloadFileKeys"
        >
          <a-button icon="download" type="primary">Download</a-button>
        </download-csv>
      </a>
      <a-row style="margin-bottom: 16px">
        <MapPlot
            :id="title"
            :plot-points="plotPoints"
        />
      </a-row>
      
      <a-row>
        <a-divider>
          <span style="font-size: 20px">
            Route
          </span>
        </a-divider>
      </a-row>
      
      <a-row>
        <template>
          <a-timeline
              mode="alternate"
              class="timeline-width"
          >
            <a-timeline-item color="green">
              <template v-slot:dot>
                <a-icon type="star" style="font-size: 16px;"/>
              </template>
              <span style="color: #389e0d; font-weight: bolder; font-size: 16px">
                Start
              </span>
            </a-timeline-item>
            
            
            <a-timeline-item
                v-for="(n, day) in this.days"
                :key="dailyItinerary[day].name"
            >
              <TimelineCard :point-of-interest="dailyItinerary[day]" :visitSequence="day"/>
            </a-timeline-item>
            
            <a-timeline-item color="red">
              <template v-slot:dot>
                <a-icon type="stop"/>
              </template>
              <span style="color: red; font-weight: bolder; font-size: 16px">
                End
              </span>
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
    import downloadCsv from 'vue-json-csv';

    export default {
        name: "ItineraryCard",
        components: {TimelineCard, MapPlot, downloadCsv},
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
        data() {
            return {
                downloadFileKeys: ['name', 'price', 'description', 'visitDuration', 'openingHours', 'website', 'address']
            }
        },
        computed: {
            downloadData() {
                return Object.keys(this.dailyItinerary).map(day => {
                    const dayDetails = this.dailyItinerary[day];
                    const {name, price, description, hours: visitDuration, openingHours, website, address} = dayDetails;
                    return ({
                        name,
                        description,
                        price,
                        visitDuration,
                        openingHours,
                        website,
                        address
                    })
                })
            },
            fileName() {
                const title = this.title.replaceAll(" ", "_").toLocaleLowerCase();
                return `${title}_${this.$store.state.form.city}.csv`;
            },
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