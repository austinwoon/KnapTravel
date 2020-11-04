<template>
  <div>
    
    <ItineraryCard
        v-for="(dailyItinerary, i) in results"
        :key="i+'itineraryCard'"
        :dailyItinerary="dailyItinerary"
        :title="`Day ${i + 1} Schedule`"
    />
  
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