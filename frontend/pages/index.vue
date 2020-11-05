<template>
  <div class="container">
    <div>
      <a-card
          title="Select City"
          class="card-style"
      >
        <a-select
            v-model="form.city"
            placeholder="Please select city to visit"
            @change="handleSelectCity()"
            style="width: 120px"
            class="card-style"
        >
          <template v-for="city in cities">
            <a-select-option :value="city">
              {{ city }}
            </a-select-option>
          </template>
        </a-select>
      </a-card>
      
      <a-card
          title="Travel Destination"
          class="card-style"
          v-if="form.city"
          :loading="loading"
      >
        <a-form-model layout="vertical" :model="form">
          
          <a-form-model-item label="Length of Stay (Days)">
            <a-input-number v-model="form.lengthOfStay" :min="1"/>
          </a-form-model-item>
          
          <a-form-model-item label="Daily Max Visiting Time">
            <a-input-number v-model="form.timeConstraint" :min="1" :max="20"/>
          </a-form-model-item>
          
          <a-form-model-item label="Activity Interests">
            <template v-for="tag in form.tags">
              <a-checkable-tag
                  :key="tag"
                  :checked="form.selectedTags.indexOf(tag) > -1"
                  @change="checked => handleChange(tag, checked)"
              >
                {{ tag }}
              </a-checkable-tag>
            </template>
          </a-form-model-item>
          
          <a-form-model-item>
            <NuxtLink to="/results">
              <a-button
                  type="primary"
                  style="margin-right: 16px"
                  @click="submitForm"
              >
                Generate Itinerary
              </a-button>
            </NuxtLink>
            
            <a-button>
              Cancel
            </a-button>
          </a-form-model-item>
        </a-form-model>
      </a-card>
    </div>
  </div>
</template>

<script>
    import {BACKEND_URL} from "../assets/constants";

    export default {
        data() {
            return {
                cities: [],
                loading: true,
                form: {
                    city: "",
                    lengthOfStay: 1,
                    timeConstraint: 0,
                    tags: [],
                    selectedTags: [],
                }
            }
        },
        async mounted() {
            try {
                const { data } = await this.$axios.get(`${BACKEND_URL}/getCities`);
                const { availableCities } = data;
                this.cities = availableCities;
            } catch {
                this.cities = ["Tokyo", "New York"];
            }
        },
        methods: {
            async handleSelectCity() {
                this.loading = true;
                const getTagsUrl = `${BACKEND_URL}/getTags/${this.form.city}`;

                try {
                    const { tags } = await this.$axios.$get(getTagsUrl);
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
  .card-style {
    max-width: 50%;
  }
</style>
