<template>
  <div class="container">
    <div>
      <a-card title="Travel Destination" class="card-style">
        <a-form-model layout="vertical" :model="form">
          <a-form-model-item label="City">
            <a-select v-model="form.city" placeholder="Please select city to visit">
              <template v-for="city in cities">
                <a-select-option value="city">
                  {{ city }}
                </a-select-option>
              </template>
            </a-select>
          </a-form-model-item>

          <a-form-model-item label="Length of Stay (Days)">
            <a-input-number v-model="form.lengthOfStay" :min="1"/>
          </a-form-model-item>

          <a-form-model-item label="Activity Interests">
            <template v-for="tag in form.tags">
              <a-checkable-tag :key="tag"
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
export default {
  data() {
    return {
      cities: [
        "Tokyo"
      ],
      form: {
        city: "",
        lengthOfStay: 1,
        tags: ['sports',
          'sport venue',
          'drinks',
          'music venue',
          'cuisine',
          'bridge',
          'amusement ride',
          'character',
          'university',
          'theatre',
          'skyscraper',
          'options for meal times',
          'places to eat and drink',
          'eat and drink',
          'area',
          'buddhist architecture',
          'club',
          'shopping centre',
          'neighbourhood',
          'shrine',
          'dancing',
          'amusement parks',
          'temple',
          'department store',
          'shows, theatres and music',
          'top attractions',
          'architectural style',
          'art museums',
          'sight',
          'nightlife',
          'architect',
          'park',
          'relax in a park',
          'shopping',
          'museums',
          'activities',
          'districts',
          'sightseeing'],
        selectedTags: [],
      }
    }
  },
  methods: {
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
