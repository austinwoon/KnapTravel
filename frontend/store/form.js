export const state = () => ({
  city: "",
  selectedTags: [],
  lengthOfStay: 4,
  timeConstraint: 8,
  tags: [],
});

export const mutations = {
  updateState(state, formInputs) {
    const { city, lengthOfStay, selectedTags, timeConstraint, tags } = formInputs;
    state.city = city;
    state.lengthOfStay = lengthOfStay;
    state.selectedTags = selectedTags;
    state.timeConstraint = timeConstraint;
    state.tags = tags;
  },
  refreshState(state) {
    state = {
      city: "Tokyo",
      selectedTags: [],
      lengthOfStay: 4,
      timeConstraint: 8,
      tags: [],
    }
  }
};