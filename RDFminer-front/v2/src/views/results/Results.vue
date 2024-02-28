<template>
    <div v-if="Object.keys($store.state.selectedResults).length === 0">
        You must select a specific project to analyse its results
    </div>
    <div v-else>
        <h5>Entities</h5>
        <Entities></Entities>
        <br />
        <h5>Visualisations</h5>
        <Charts></Charts>
        <br />
        <CButton color="success" @click="$store.commit('downloadResults')">download the results file</CButton>
    </div>
</template>

<script>
import Entities from './Entities.vue';
import Charts from './Charts.vue';

export default {
    name: 'Results',
    components: {
        Entities, Charts
    },
}
</script>

<style scoped>
.customizedAccordion {
    --cui-accordion-btn-color: rgb(14, 14, 163);
}
</style>
  