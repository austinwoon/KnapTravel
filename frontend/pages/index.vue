<template>
  <div>
    <div class="container">
      <a-row class="searchBar background-img" type="flex" align="middle" justify="center">
        <ResultsFormInput homePage/>
      </a-row>
    </div>
    
    <a-row style="margin-top: 16px">
      
      <a-row type="flex" justify="center" align="middle">
        <a-divider>
          <span style="font-size: 30px; letter-spacing: 8px">EXPLORE  ITINERARIES</span>
        </a-divider>
        
      </a-row>
      
      <a-row class="container" type="flex" :gutter="[24, 24]">
        <a-col v-for="data in presetData" :key="data.title">
          <div @click="handleClickImageCard(data)">
            <NuxtLink to="/results">
              <itinerary-image-card
                  :cover-img="data.imgSrc"
                  :title="data.title"
                  :form-data="data.formData"
              />
            </NuxtLink>
          </div>
        </a-col>
      </a-row>
    </a-row>
  </div>
</template>

<script>
    import {BACKEND_URL} from "../assets/constants";
    import ItineraryImageCard from "../components/ItineraryImageCard";
    import ResultsFormInput from "../components/ResultsFormInput";

    export default {
        components: {ResultsFormInput, ItineraryImageCard},
        data() {
            return {
                cities: [],
                loading: true,
                presetData: [
                    {
                        title: 'Four Day Trip to Tokyo',
                        imgSrc: 'tokyo',
                        formData: {
                            city: "Tokyo",
                            lengthOfStay: 4,
                            timeConstraint: 8,
                            tags: [],
                            selectedTags: [],
                        },
                    },
                    {
                        title: 'Three Day Trip to New York',
                        imgSrc: 'new-york-city',
                        formData: {
                            city: "New York City",
                            lengthOfStay: 3,
                            timeConstraint: 8,
                            tags: [],
                            selectedTags: [],
                        },
                    },
                    {
                        title: 'Two Day Trip to Osaka',
                        imgSrc: 'osaka',
                        formData: {
                            city: "Osaka",
                            lengthOfStay: 2,
                            timeConstraint: 8,
                            tags: [],
                            selectedTags: [],
                        },
                    },
                    {
                        title: 'Four Day Trip to Taipei',
                        imgSrc: 'taipei',
                        formData: {
                            city: "Taipei",
                            lengthOfStay: 4,
                            timeConstraint: 8,
                            tags: [],
                            selectedTags: [],
                        },
                    },
                    {
                        title: 'Two Day Trip to Paris',
                        imgSrc: 'paris',
                        formData: {
                            city: "Paris",
                            lengthOfStay: 2,
                            timeConstraint: 8,
                            tags: [],
                            selectedTags: [],
                        },
                    },
                ],
                form: {
                    city: "",
                    lengthOfStay: 1,
                    timeConstraint: 1,
                    tags: [],
                    selectedTags: [],
                }
            }
        },
        async mounted() {
            try {
                const {data} = await this.$axios.get(`${BACKEND_URL}/getCities`);
                const {availableCities} = data;
                this.cities = availableCities;
            } catch {
                this.cities = ["Tokyo", "New York"];
            }
        },
        methods: {
            async handleClickImageCard(data) {
                this.$store.commit('form/updateState', data.formData);
            },
            async handleSelectCity() {
                this.loading = true;
                const getTagsUrl = `${BACKEND_URL}/getTags/${this.form.city}`;

                try {
                    const {tags} = await this.$axios.$get(getTagsUrl);
                    this.form.tags = tags;
                    this.loading = false;
                } catch (e) {
                    console.log(e);
                }

            },
            submitForm() {
                // TODO(Austin): Add validation if not laze
                this.$store.commit('form/updateState', this.form);
            },
            handleChange(tag, checked) {
                const {selectedTags} = this.form;
                this.form.selectedTags = checked ? [...selectedTags, tag] : selectedTags.filter(t => t !== tag);
            }
        }
    }
</script>

<style>
  .container {
    justify-content: center;
    display: flex;
  }
  
  .background-img {
    background-image: url("~assets/img/background.jpg");
    background-size: cover;
    background-color: #6DB3F2;
    background-position: center center;
  }
  
  .searchBar {
    height: 50vh;
    width: 100%;
  }
</style>
