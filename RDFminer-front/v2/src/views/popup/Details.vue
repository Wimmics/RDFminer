<template>
    <CModal :visible="$store.state.detailsPopupVisible" @close="$store.commit('toggleDetailsPopup', {})" alignment="center" scrollable size="xl">
        <CModalHeader>
            <CModalTitle>Settings of <b>{{ $store.state.selectedProject.projectName }}</b></CModalTitle>
        </CModalHeader>
        <CModalBody>
            <CCard>
                <CCardTitle>
                    Global settings
                </CCardTitle>
                <CCardBody>
                    <!-- SPARQL Endpoint -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel>RDF data graph</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            {{ $store.state.selectedProject.namedDataGraph }}
                        </CCol>
                    </CRow>
                    <!-- Prefixes -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel>Prefixes</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormTextarea readonly>{{ $store.state.selectedProject.prefixes }}</CFormTextarea>
                        </CCol>
                    </CRow>
                    <!-- Grammatical Evolution -->
                </CCardBody>
            </CCard>
            <br />
            <CCard v-if="$store.state.selectedProject.mod == 1 || $store.state.selectedProject.mod == 3">
                <CCardTitle>
                    Grammatical evolution settings
                </CCardTitle>
                <CCardBody>
                    <!-- Axioms ? Shapes ? -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Type of entities</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput :value="$store.state.selectedProject.mod == 3 ? 'SHACL Shapes' : 'OWL Axioms'" readonly
                                plain-text style="font-weight: bold;" />
                        </CCol>
                    </CRow>
                    <!-- Probabilistic SHACL ? -->
                    <CRow v-if="$store.state.selectedProject.probShaclP != 0">
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Probabilistic SHACL:</CFormLabel>
                        </CCol>
                        <CCol sm="2">
                            <CFormLabel class="col-form-label">Significance level</CFormLabel>
                        </CCol>
                        <CCol sm="2">
                            <CFormInput :value="$store.state.selectedProject.probShaclAlpha * 100 + '%'" readonly plain-text />
                        </CCol>
                        <CCol sm="2">
                            <CFormLabel class="col-form-label">P-Value</CFormLabel>
                        </CCol>
                        <CCol sm="2">
                            <CFormInput :value="$store.state.selectedProject.probShaclP * 100 + '%'" readonly plain-text />
                        </CCol>
                    </CRow>
                    <!-- BNF Grammar -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">BNF Grammar</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormTextarea readonly>{{ $store.state.selectedProject.grammar }}</CFormTextarea>
                        </CCol>
                    </CRow>
                    <!-- Chromosome size -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Chromosome size</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput :value="$store.state.selectedProject.sizeChromosome" readonly plain-text />
                        </CCol>
                    </CRow>
                    <!-- Pop size / Effort -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Population size</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput :value="$store.state.selectedProject.populationSize" readonly plain-text />
                        </CCol>
                    </CRow>
                    <CRow v-if="$store.state.selectedProject.stopCriterion == 1">
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Time allocated (min)</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput :value="$store.state.selectedProject.maxMiningTime" readonly plain-text />
                        </CCol>
                    </CRow>
                    <CRow v-else>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Effort</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput :value="$store.state.selectedProject.effort" readonly plain-text />
                        </CCol>
                    </CRow>
                    <!-- Elite -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Elite selection rate</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput :value="($store.state.selectedProject.eliteSelectionRate * 100) + '%'" readonly plain-text />
                        </CCol>
                    </CRow>
                    <!-- Selection / Crossover / Mutation-->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Selection</CFormLabel>
                        </CCol>
                        <CCol sm="4">
                            <CFormInput
                                :value="selectionType[$store.state.selectedProject.selectionType - 1].description + ' (' + ($store.state.selectedProject.selectionRate * 100) + '%)'"
                                readonly plain-text />
                        </CCol>
                        <CCol sm="4">
                            <CFormInput
                                :value="'Tournament size: ' + ($store.state.selectedProject.tournamentSelectionRate * 100) + '% of the population'"
                                readonly plain-text />
                        </CCol>
                    </CRow>
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Crossover</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput
                                :value="crossoverType[$store.state.selectedProject.crossoverType - 1].description + ' (' + ($store.state.selectedProject.proCrossover * 100) + '%)'"
                                readonly plain-text />
                        </CCol>
                    </CRow>
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Mutation</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput
                                :value="mutationType[$store.state.selectedProject.mutationType - 1].description + ' (' + ($store.state.selectedProject.proMutation * 100) + '%)'"
                                readonly plain-text />
                        </CCol>
                    </CRow>
                </CCardBody>
            </CCard>

            <CCard v-else>
                <CCardTitle>
                    Assessment settings
                </CCardTitle>
                <CCardBody>
                    <!-- Axioms ? Shapes ? -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Type of entities</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormInput :value="$store.state.selectedProject.mod == 4 ? 'SHACL Shapes' : 'OWL Axioms'" readonly
                                plain-text style="font-weight: bold;" />
                        </CCol>
                    </CRow>
                    <!-- Probabilistic SHACL ? -->
                    <CRow v-if="$store.state.selectedProject.probShaclP != 0">
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Probabilistic SHACL:</CFormLabel>
                        </CCol>
                        <CCol sm="2">
                            <CFormLabel class="col-form-label">Significance level</CFormLabel>
                        </CCol>
                        <CCol sm="2">
                            <CFormInput :value="$store.state.selectedProject.probShaclAlpha * 100 + '%'" readonly plain-text />
                        </CCol>
                        <CCol sm="2">
                            <CFormLabel class="col-form-label">P-Value</CFormLabel>
                        </CCol>
                        <CCol sm="2">
                            <CFormInput :value="$store.state.selectedProject.probShaclP * 100 + '%'" readonly plain-text />
                        </CCol>
                    </CRow>
                    <!-- Input file -->
                    <CRow>
                        <CCol sm="4">
                            <CFormLabel class="col-form-label">Entities provided</CFormLabel>
                        </CCol>
                        <CCol sm="8">
                            <CFormTextarea readonly>{{ $store.state.selectedProject.shapes }}</CFormTextarea>
                        </CCol>
                    </CRow>
                </CCardBody>
            </CCard>

        </CModalBody>
    </CModal>
</template>
  
<script>
// import LoginForm from '@/vues/auth/LoginForm.vue';
// https://coreui.io/vue/docs/components/modal.html
import {
    CModal, CModalHeader, CModalTitle, CModalBody, CCol, CRow, CFormLabel, CFormInput,
    CFormTextarea, CCard, CCardBody, CCardTitle
} from "@coreui/vue";
import { get } from "@/tools/api";

export default {
    name: 'DetailsPopup',
    components: {
        CModal, CModalHeader, CModalTitle, CModalBody, CCol, CRow, CFormLabel,
        CFormInput, CFormTextarea, CCard, CCardBody, CCardTitle
    },
    // props: {
    //     enable: {
    //         type: Boolean
    //     },
    //     data: {
    //         type: Object
    //     }
    // },
    data() {
        return {
            selectionType: {},
            crossoverType: {},
            mutationType: {},
        }
    },
    methods: {
        async setupParams() {
            const params = (await get("api/params", {}))[0];
            // selection
            this.selectionType = params.selectionType.values;
            // crossover
            this.crossoverType = params.crossoverType.values;
            // mutation
            this.mutationType = params.mutationType.values;
        }
    },
    mounted() {
        this.setupParams();
    }
}
</script>