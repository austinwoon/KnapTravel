<template>
  <div style="width:100vw">
    <a-row type="flex" gutter="md">
      <a-col
          style="margin: 0 16px 0 16px"
          :span="7"
          :xs="{ span: 24 }"
          :sm="{ span: 24 }"
          :md="{ span: 24 }"
          :lg="{ span: 11 }"
          :xl="{ span: 11 }"
          :xxl="{ span: 7 }"
          v-for="(dailyItinerary, i) in results"
          :key="i+'itineraryCard'"
      >
        <ItineraryCard
            :dailyItinerary="dailyItinerary"
            :title="`Day ${i + 1} Schedule`"
        />
      </a-col>
    </a-row>
  
  </div>
</template>

<script>
    import {BACKEND_URL} from "../assets/constants";
    import ItineraryCard from "../components/ItineraryCard";
    import MapPlot from "../components/MapPlot";

    export default {
        components: {MapPlot, ItineraryCard},
        data() {
            return {
                results: []
            }
        },
        name: "results",
        computed: {
            formData() {
                return this.$store.state.form;
            }
        },
        mounted() {
            const URL = `${BACKEND_URL}/getItinerary`;
            //TODO (AUSTIN): Remove this dummy data
            const requestBody = this.formData.city ? this.formData : {
                lengthOfStay: 3,
                timeConstraint: 8,
                selectedTags: ["Museum"],
                city: "Tokyo",
            };

            const {lengthOfStay: days, timeConstraint, selectedTags: interests, city} = requestBody;
            this.$axios.$post(URL, {
                days,
                timeConstraint,
                interests,
                city,
            }).then(({data}) => {
                this.results = data;
            }).catch(e => {
                console.log(e);
            })
        }
    }
</script>

<style scoped>

</style>