<template>
  <div>
    <div class="container">
      <a-row class="background-img background-container">
        <div class="dark-overlay"></div>
        <a-row class="background-container">
          <span class="main-title">KNAPTRAVEL</span>
          <span class="main-subtitle">DISCOVER PAIN-FREE HOLIDAY PLANNING TODAY</span>
        </a-row>
        <a-row>
          <ResultsFormInput homePage/>
        </a-row>
      </a-row>
    </div>
    
    <a-row class="secondary-background">
      <a-row type="flex" justify="center" align="middle">
        <a-divider>
          <span class="secondary-subtitle">EXPLORE  ITINERARIES</span>
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
    height: 50vh;
    width: 100%;
  }
  
  .background-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  
  .dark-overlay {
    height: 100%;
    width: 100%;
    background-color: rgba(0, 0, 0, .25);
    position: absolute;
    top: 0;
    left: 0;
  }
  
  .main-title {
    color: aqua;
    font-size: 80px;
    font-weight: bolder;
    letter-spacing: 35px;
    text-shadow: 3px 3px black;
  }
  
  .main-subtitle {
    font-size: 25px;
    font-weight: bold;
    letter-spacing: 5px;
    color: whitesmoke;
    text-shadow: 1px 1px darkslategrey;
    text-transform: uppercase;
    margin-bottom: 8px;
  }
  
  .secondary-subtitle {
    color: black;
    /*text-shadow: 2px 2px darkslategrey;*/
    font-size: 35px;
    letter-spacing: 8px;
  }
  
  .secondary-background {
    /*background-image: linear-gradient(to top, #cfd9df 0%, #e2ebf0 100%);*/
    padding: 8px 0 24px 0;
    min-height: 50vh;
    height: 100%;
  }

</style>
