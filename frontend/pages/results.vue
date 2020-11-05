<template>
  <a-row>
    
    <a-row type="flex" align="middle" justify="center">
      <ResultsFormInput
          style="margin-top: 24px"
          v-on:generateItinerary="generateItinerary"
      />
    </a-row>
    
    <a-row
        style="margin-top: 24px; letter-spacing: 5px; text-transform: uppercase"
    
    >
      <a-row
          type="flex"
          align="middle"
          justify="center"
      >
        <h1>Your Itinerary</h1>
      </a-row>
      
      <a-row
          type="flex"
          align="middle"
          justify="center"
      >
        <span>Horizontal Scroll to View  More</span>
        <a-icon type="arrow-right"/>
      </a-row>
    </a-row>
    
    <div v-if="!formData.city">
      <a-row type="flex" justify="center" align="middle">
        <h1> PLEASE SELECT A CITY </h1>
      </a-row>
    </div>
    
    <div v-else>
      <a-spin :spinning="loading" size="large" tip="Loading">
        <a-row class="daily-itinerary-container">
          <a-col
              style="margin: 24px 24px 24px 24px; min-width: 800px"
              v-for="(dailyItinerary, i) in results"
              :key="key + '' + i"
          >
            <ItineraryCard
                :dailyItinerary="dailyItinerary"
                :title="`Day ${i + 1} Schedule`"
            />
          </a-col>
        </a-row>
      </a-spin>
    </div>
  
  </a-row>
</template>

<script>
    import {BACKEND_URL} from "../assets/constants";
    import ItineraryCard from "../components/ItineraryCard";
    import MapPlot from "../components/MapPlot";
    import ResultsFormInput from "../components/ResultsFormInput";

    export default {
        components: {ResultsFormInput, MapPlot, ItineraryCard},
        data() {
            return {
                results: [],
                loading: false,
            }
        },
        name: "results",
        computed: {
            formData() {
                return this.$store.state.form;
            },
            key() {
                const {lengthOfStay, timeConstraint, city} = this.$store.state.form;
                return city + lengthOfStay + timeConstraint;
            }
        },
        methods: {
            async generateItinerary() {
                this.loading = true;
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
                    this.loading = false;
                }).catch(e => {
                    console.log(e);
                })
            }
        },
        mounted() {
            this.generateItinerary();
        }
    }
</script>

<style scoped>
  .daily-itinerary-container {
    display: flex;
    overflow: auto;
  }

</style>