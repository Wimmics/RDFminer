<template>
    <div v-if="Object.keys($store.state.selectedResults).length === 0">
        You must select a specific project to analyse its results
    </div>
    <div v-else>
        <CAccordion :active-item-key="1" always-open>
            <CAccordionItem :item-key="1">
                <CAccordionHeader>Progression</CAccordionHeader>
                <CAccordionBody>
                    <Progression></Progression>
                </CAccordionBody>
            </CAccordionItem>
            <CAccordionItem :item-key="2">
                <CAccordionHeader>Entities</CAccordionHeader>
                <CAccordionBody>
                    <Entities></Entities>
                </CAccordionBody>
            </CAccordionItem>
            <CAccordionItem :item-key="3">
                <CAccordionHeader>Visualisations</CAccordionHeader>
                <CAccordionBody>
                    <Charts></Charts>
                </CAccordionBody>
            </CAccordionItem>
        </CAccordion>
        <br />
        <CRow class="mb-3">
            <CCol sm="2">
                <CButton color="success" @click="$store.commit('downloadResults')">download full results</CButton>
            </CCol>
            <CCol sm="2">
                <CButton color="success" @click="$store.commit('downloadShapes')">download shapes file</CButton>
            </CCol>
        </CRow>
    </div>
</template>

<script>
import Entities from './components/Entities.vue';
import Charts from './components/Charts.vue';
import Progression from './components/Progression.vue';

export default {
    name: 'Results',
    components: {
        Entities, Charts, Progression
    },
}
</script>

<style scoped>
.customizedAccordion {
    --cui-accordion-btn-color: rgb(14, 14, 163);
}
</style>
  