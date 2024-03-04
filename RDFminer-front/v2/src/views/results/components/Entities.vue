<template>
    <div class="scroll">
        <CTable align="middle" class="mb-0 border" striped responsive hover>
            <CTableHead color="light">
                <CTableRow>
                    <CTableHeaderCell v-for="header in headers" :key="header" scope="col">{{ header }}</CTableHeaderCell>
                </CTableRow>
            </CTableHead>
            <CTableBody>
                <CTableRow v-for="entity in $store.state.selectedResults.entities" :key="entity" :color="getColor(entity.acceptance)">
                    <CTableHeaderCell scope="row"><p v-html="detectAndHighlightLink(entity.phenotype)"></p></CTableHeaderCell>
                    <CTableDataCell>{{ entity.referenceCardinality }}</CTableDataCell>
                    <CTableDataCell>{{ entity.numExceptions }}</CTableDataCell>
                    <CTableDataCell>{{ entity.numConfirmations }}</CTableDataCell>
                    <CTableDataCell>{{ entity.likelihood }}</CTableDataCell>
                    <CTableDataCell>{{ entity.fitness }}</CTableDataCell>
                    <CTableDataCell>{{ entity.acceptance }}</CTableDataCell>
                    <CTableDataCell><CIcon v-if="entity.elite" icon="cil-badge" /></CTableDataCell>
                </CTableRow>
            </CTableBody>
            <CTableFoot>
            </CTableFoot>
        </CTable>
    </div>
</template>


<script>
import { CTable, CTableHead, CTableRow, CTableBody, CTableFoot, CTableDataCell, CTableHeaderCell, CIcon } from "@coreui/vue";

export default {
    name: 'Entities',
    components: {
        CTable, CTableHead, CTableRow, CTableBody, CTableFoot, CTableDataCell, CTableHeaderCell, CIcon
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
        };
    },
    mounted() {
        // this.entities = this.results.entities;
        // Header
        this.headers = ["SHACL Shape", "Reference Cardinality", "#Exceptions", "#Confirmations", "Likelihood", "Fitness Score", "Acceptance", "Elite"];
    }
}
</script>

<style scoped>
.scroll {
    height: 700px;
    overflow-x: hidden;
    overflow-y: auto;
}
</style>
