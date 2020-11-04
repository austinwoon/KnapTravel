export const state = () => ({
  city: "",
  selectedTags: [],
  lengthOfStay: 0,
})

export const mutations = {
  updateState(state, formInputs) {
    const { city, lengthOfStay, selectedTags, timeConstraint } = formInputs;
    state.city = city;
    state.lengthOfStay = lengthOfStay;
    state.selectedTags = selectedTags;
    state.timeConstraint = timeConstraint;
  },
  refreshState(state) {
    state = {
      city: "",
      selectedTags: [],
      lengthOfStay: 0,
    }
  }
}