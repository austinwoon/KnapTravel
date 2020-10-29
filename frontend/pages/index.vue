<template>
  <div class="container">
    <div>
      <a-card title="Travel Destination">
        <a-form-model :model="form" layout="horizontal" :labelCol="labelCol" :wraperCol="wrapperCol">
          <a-form-model-item label="City">
            <a-input v-model="form.city"/>
          </a-form-model-item>

          <a-form-model-item label="Length of Stay (Days)">
            <a-input-number v-model="form.lengthOfStay" :min="1"/>
          </a-form-model-item>

          <div>
            <span>Activity Interests: </span>
            <template v-for="tag in form.tags">
              <a-checkable-tag :key="tag"
                               :checked="form.selectedTags.indexOf(tag) > -1"
                               @change="checked => handleChange(tag, checked)"
              >
                {{ tag }}
              </a-checkable-tag>
            </template>
          </div>

        </a-form-model>
      </a-card>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      labelCol: {span: 4},
      wrapperCol: {span: 14},
      form: {
        city: "",
        lengthOfStay: 1,
        interests: [],
        tags: ["Sports", "Outdoors", "Family Friendly", "Restaurants", "Food", "Hiking", "Adventure"],
        selectedTags: [],
      }
    }
  },
  methods: {
    handleChange(tag, checked) {
      const {selectedTags} = this.form;
      const nextTags = checked ? [...selectedTags, tag] : selectedTags.filter(t => t !== tag);
      this.form.selectedTags = nextTags;
    }
  }
}
</script>

<style>
</style>
