<template>
    <h1 style="text-align: center;">Summary Dashboard for the <i>{{ results.projectName }}</i> project</h1>
    <CAccordion class="customizedAccordion" :active-item-key="1" always-open>
        <CAccordionItem :item-key="1">
            <CAccordionHeader>Dashboard</CAccordionHeader>
            <CAccordionBody>
                <!-- <VisuEntities v-if="isReady" :results="results"></VisuEntities> -->
                <!-- <VHeader v-if="isReady && results.mod == 1 || this.results.mod == 3" :results="results" :path="path" :task="'Mining'"></VHeader> -->
                <br/>
                <VueStatistics v-if="isReady" :results="results"></VueStatistics>
                <!-- Eval -->
                <!-- <VisAssessment v-if="isReady && task=='Assessment'" :results="results" :path="path" :task="'Assessment'"></VisAssessment> -->
                <br/>
                <BubbleEntities v-if="isReady" :results="results"></BubbleEntities>
            </CAccordionBody>
        </CAccordionItem>
        <CAccordionItem :item-key="2">
            <CAccordionHeader>Entities</CAccordionHeader>
            <CAccordionBody>
                <VisuEntities v-if="isReady" :results="results"></VisuEntities>
            </CAccordionBody>
        </CAccordionItem>
        <!-- <CAccordionItem :item-key="2" v-if="task=='Mining'">
            <CAccordionHeader>Statistics</CAccordionHeader>
            <CAccordionBody>
            </CAccordionBody>
        </CAccordionItem> -->
        <CAccordionItem :item-key="3">
            <CAccordionHeader>Console log</CAccordionHeader>
            <!-- <CAccordionBody>
                <ConsoleLog v-if="isReady" :path="path"></ConsoleLog>
            </CAccordionBody> -->
        </CAccordionItem>
    </CAccordion>
</template>

<script>
import { CAccordion, CAccordionItem, CAccordionHeader, CAccordionBody } from '@coreui/vue';
// import { useCookies } from "vue3-cookies";
import VueStatistics from './plot/Statistics.vue';
import VisuEntities from './Entities.vue';
// import ConsoleLog from './ConsoleLog.vue';
// import VHeader from './plot/Header.vue';
// import VisAssessment from './plot/Assessment.vue';
// import axios from 'axios';
import BubbleEntities from './plot/BubbleEntities.vue';
import { get } from '@/tools/api';

export default {
    name: 'VueVisualisation',
    components: {
        VueStatistics, VisuEntities, CAccordion, CAccordionItem, CAccordionHeader, 
        CAccordionBody, BubbleEntities
    },
    data() {
        return {
            // cookies: useCookies(["token", "id"]).cookies,
            id: "",
            results: {},
            isReady: false,
        };
    },
    methods: {
        async getResults(id) {
            this.results = await get("api/results", { resultsID: id });
            this.isReady = true;
        }
    },
    mounted() {
        // console.log("TASK:" + this.task);
        this.id = this.$route.params.resultsID;
        // get results from API
        this.getResults(this.id);
        // console.log(this.task);
    },
}
</script>

<style scoped>
.customizedAccordion {
    --cui-accordion-btn-color: rgb(14, 14, 163);
}
</style>
  