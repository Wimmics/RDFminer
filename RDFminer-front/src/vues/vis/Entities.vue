<template>
    <div class="scroll">
        <CTable striped hover>
            <CTableHead color="light">
                <CTableRow>
                    <CTableHeaderCell v-for="header in headers" :key="header" scope="col">{{ header }}</CTableHeaderCell>
                </CTableRow>
            </CTableHead>
            <CTableBody>
                <CTableRow v-for="entity in entities" :key="entity" :color="getColor(entity.acceptance)">
                    <CTableHeaderCell scope="row" v-html="detectAndHighlightLink(entity.phenotype)"></CTableHeaderCell>
                    <CTableDataCell>{{ entity.referenceCardinality }}</CTableDataCell>
                    <CTableDataCell>{{ entity.numExceptions }}</CTableDataCell>
                    <CTableDataCell>{{ entity.numConfirmations }}</CTableDataCell>
                    <CTableDataCell>{{ entity.likelihood }}</CTableDataCell>
                    <CTableDataCell>{{ entity.fitness }}</CTableDataCell>
                    <CTableDataCell>{{ entity.acceptance }}</CTableDataCell>
                    <CTableDataCell>
                        <CImage v-if="entity.elite" src="/assets/crown.png" width="20" height="20" />
                    </CTableDataCell>
                </CTableRow>
            </CTableBody>
            <CTableFoot>
            </CTableFoot>
        </CTable>
    </div>
</template>


<script>
// https://hc200ok.github.io/vue3-easy-data-table-doc
// import { ref } from "vue";
import { CTable, CTableHead, CTableRow, CTableBody, CTableFoot, CTableDataCell, CTableHeaderCell, CImage } from "@coreui/vue";
// import { toRaw } from "vue";
// import Vue3EasyDataTable from 'vue3-easy-data-table';
// import { entities } from '../../data/results_1.json';
// import 'vue3-easy-data-table/dist/style.css';

export default {
    name: 'VisuEntities',
    props: {
        results: {
            type: Object
        }
    },
    components: {
        CTable, CTableHead, CTableRow, CTableBody, CTableFoot, CTableDataCell, CTableHeaderCell, CImage
    },
    methods: {
        getColor(accepted) {
            if (accepted)
                return "success";
            return "danger";
        },
        detectAndHighlightLink(phenotype) {
            //eslint-disable-next-line
            phenotype = phenotype.replace(/<http:\/\/[a-zA-Z0-9\/\.\#\-]*>/g, (match) => {
                return `<<a href="${match.replace(/<|>/g, '')}" target="_blank">${match.replace(/<|>/g, '')}</a>>`;
            });
            return phenotype;
        }
    },
    data() {
        return {
            headers: [],
            entities: [],
            phenotypes: [],
            // searchValue: ref("Yolo")
        };
    },
    mounted() {
        // Items
        // console.log("entities: " + JSON.stringify(this.results.entities));
        // this.results.entities.forEach((entity) => {
        //     if (this.phenotypes.indexOf(entity.phenotype) != -1) {
        //         console.log("~doublon: " + entity.phenotype)
        //     }
        //     this.entities.push(entity);
        // });
        this.entities = this.results.entities;
        // Header
        this.headers = ["SHACL Shape (" + this.entities.length + ")", "Reference Cardinality", "#Exceptions", "#Confirmations", "Likelihood", "Fitness Score", "Acceptance", "Elite"];
    }
}
</script>

<style scoped>
.scroll {
    height: 400px;
    overflow-x: hidden;
    overflow-y: auto;
}
</style>
