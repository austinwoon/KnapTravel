<template>
  <a-card
      title="Select City"
      class="card-style"
  >
    <a-select
        v-model="form.city"
        placeholder="Please select city to visit"
        @change="handleSelectCity()"
        style="width: 120px"
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
    <a-form-model layout="inline" :model="form">
      
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
</template>

<script>
    export default {
        name: "OldForm"
    }
</script>

<style scoped>

</style>