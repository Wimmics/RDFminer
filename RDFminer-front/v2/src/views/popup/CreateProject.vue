<template>
    <CModal :visible="$store.state.createProjectPopupVisible" @close="$store.commit('toggleCreateProjectPopup')" alignment="center" scrollable size="xl">
        <CModalHeader>
            <CModalTitle>Creation form</CModalTitle>
        </CModalHeader>
        <CModalBody>
            <CForm novalidate :validated="validated">
                <CCard>
                    <CCardTitle class="text-center">
                        General Settings
                    </CCardTitle>
                    <CCardBody>
                        <!-- 
                    Project Name 
                -->
                        <CRow class="mb-3">
                            <CFormLabel class="col-sm-2 col-form-label"><b>{{ projectNameDescription }}</b></CFormLabel>
                            <CCol sm="10">
                                <CFormInput type="email" class="col-sm-2 col-form-label"
                                    placeholder="Example: MyProject; my-project; my_project" v-model="selectedProjectName" required
                                    :feedback-invalid="errorMessage" :invalid="selectedProjectName == '' || alreadyExist" />
                            </CCol>
                        </CRow>
                        <!-- 
                    Load parameters from existing project
                -->
                        <CRow class="mb-3">
                            <CFormLabel class="col-sm-2 col-form-label">Load parameters from existing project</CFormLabel>
                            <CCol sm="10">
                                <CFormSelect v-model="projectToLoad">
                                    <option selected disabled>Select an existing project settings </option>
                                    <option v-for="project in $store.state.projects" :key="project" :value="project.projectName">{{ project.projectName }}</option>
                                </CFormSelect>
                            </CCol>
                        </CRow>
                        <!-- 
                    Mod selection 
                -->
                        <CRow class="mb-3">
                            <CFormLabel class="col-sm-2 col-form-label"><b>I would like to ...</b></CFormLabel>
                            <CCol sm="10">
                                <CFormSelect v-model="selectedMod" @change="selectedGrammar = ''"
                                    feedback-invalid="Please, select a feature" :invalid="selectedMod == ''">
                                    <option selected disabled>Select the required mode</option>
                                    <option v-for="mod in modTypes" :key="mod" :value="mod.value">{{ mod.description }}</option>
                                </CFormSelect>
                            </CCol>
                        </CRow>
                        <!-- 
                    SPARQL Endpoint
                -->
                        <CRow class="mb-3">
                            <CFormLabel class="col-sm-2 col-form-label"><b>{{ namedDataGraphDescription }}</b></CFormLabel>
                            <CCol sm="3">
                                <CFormSelect v-model="selectedNamedDataGraph" feedback-invalid="Please, select a RDF data graph to exploit"
                                    :invalid="selectedNamedDataGraph == ''">
                                    <option selected disabled>Choose a RDF data graph</option>
                                    <option v-for="uri in namedDataGraphTypes" :key="uri" :value="uri.value">{{ uri.description }}</option>
                                </CFormSelect>
                            </CCol>
                        </CRow>
                        <!--
                    Prefixes
                -->
                        <CRow class="mb-3">
                            <CFormLabel class="col-sm-2 col-form-label"><b>{{ prefixesDescription }}</b></CFormLabel>
                            <CCol sm="3">
                                <!-- Template -->
                                <CFormSelect v-model="selectedPrefixes" feedback-invalid="Please, select a prefixes sample" required :invalid="selectedPrefixes == ''">
                                    <option selected disabled>Select the required prefixes</option>
                                    <option v-for="prefixes in prefixesTypes" :key="prefixes" :value="prefixes.value">{{ prefixes.description }}</option>
                                </CFormSelect>
                                <!-- <br /> -->
                                <!-- <p>{{ selectedPrefixes }}</p> -->
                            </CCol>
                            <CCol sm="7">
                                <!-- Content -->
                                <CFormTextarea style="color: rgb(1, 108, 157)" v-model="selectedPrefixes">{{ selectedPrefixes }}</CFormTextarea>
                            </CCol>
                        </CRow>
                    </CCardBody>
                </CCard><br />
                <!-- 
                    Depending on the mod, we load the right component 
                    Just below: Grammatical Evolution Mod
                -->
                <div v-if="selectedMod == 1 || selectedMod == 3">
                    <CCard>
                        <CCardTitle class="text-center">Grammatical Evolution</CCardTitle>
                        <CCardBody>
                            <!--
                        SHACL
                    -->
                            <div v-if="selectedMod == 3">
                                <CRow class="mb-3">
                                    <CFormLabel class="col-sm-2 col-form-label"><b>Probabilistic SHACL</b></CFormLabel>
                                    <CCol sm="2">
                                        <!-- Alpha -->
                                        <CFormLabel>{{ probShaclAlphaDescription }}</CFormLabel>
                                    </CCol>
                                    <CCol sm="2">
                                        <!-- P -->
                                        <CFormSelect v-model="selectedProbShaclAlpha" :disabled="selectedProbShaclP == 0" required>
                                            <option selected disabled>Select the required significance level</option>
                                            <option v-for="alpha in probShaclAlphaTypes" :key="alpha" :value="alpha.value">{{ alpha.description }}</option>
                                        </CFormSelect>
                                    </CCol>
                                    <CCol sm="1">
                                        <!-- p-value -->
                                        <CFormLabel>{{ probShaclPDescription }}</CFormLabel>
                                    </CCol>
                                    <CCol sm="4">
                                        <CFormRange :min="0" :max="1" :step="0.05" v-model="selectedProbShaclP" />
                                    </CCol>
                                    <CCol sm="1">
                                        <CFormLabel class="col-form-label"><b>{{ selectedProbShaclP }}</b></CFormLabel>
                                    </CCol>
                                </CRow>
                                <CAlert color="warning" v-if="selectedProbShaclP == 0">
                                    <b>Standard SHACL validation !</b> increase the <b>P-value</b> to enable the <b>probabilistic SHACL validation</b>
                                </CAlert>
                                <CAlert color="warning" v-else>
                                    <b>Probabilistic SHACL validation !</b> set the <b>P-value</b> at 0 to enable the <b>standard SHACL validation</b>
                                </CAlert>
                            </div>
                            <!--
                        BNF Grammar
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ grammarDescription }}</b></CFormLabel>
                                <CCol sm="3">
                                    <!-- Template -->
                                    <CFormSelect aria-label="select-grammar" v-model="selectedGrammar"
                                        feedback-invalid="Please select a BNF template (You can customize it !)" required
                                        :invalid="selectedGrammar == ''">
                                        <option selected disabled>Select the required template</option>
                                        <option v-for="grammar in filteredGrammarTypes" :key="grammar" :value="grammar.value">{{ grammar.description }}</option>
                                    </CFormSelect>
                                    <!-- <br /> -->
                                </CCol>
                                <CCol sm="7">
                                    <!-- Content -->
                                    <CFormTextarea style="color: rgb(1, 108, 157)">{{ selectedGrammar }}</CFormTextarea>
                                </CCol>
                                <!-- <p>filtered: {{ filteredTemplates.length }} / templates: {{ templates.length }}</p> -->
                            </CRow>
                            <!-- 
                        Size of chromosomes 
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ sizeChromosomeDescription }}</b></CFormLabel>
                                <CCol sm="9">
                                    <CFormRange :min="1" :max="20" :step="1" v-model="selectedSizeChromosome" />
                                </CCol>
                                <CCol sm="1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedSizeChromosome }}</b></CFormLabel>
                                </CCol>
                            </CRow>
                            <!-- 
                        Max Wrapping
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ maxWrapDescription }}</b></CFormLabel>
                                <CCol sm="9">
                                    <CFormRange :min="1" :max="100" :step="1" v-model="selectedMaxWrap" />
                                </CCol>
                                <CCol sm="1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedMaxWrap }}</b></CFormLabel>
                                </CCol>
                            </CRow>
                            <!--
                        Population size
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ populationSizeDescription }}</b></CFormLabel>
                                <CCol sm="9">
                                    <CFormRange :min="10" :max="1000" :step="10" v-model="selectedPopulationSize" />
                                </CCol>
                                <CCol sm="1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedPopulationSize }}</b></CFormLabel>
                                </CCol>
                            </CRow>
                            <!-- 
                        Elite Selection
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ eliteSelectionRateDescription }}</b></CFormLabel>
                                <CCol sm="9">
                                    <CFormRange :min="0.05" :max="1" :step="0.01" v-model="selectedEliteSelectionRate" />
                                </CCol>
                                <CCol sm="1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedEliteSelectionRate }}</b></CFormLabel>
                                </CCol>
                            </CRow>
                            <!-- 
                        Selection: type of selection and proportion 
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ selectionDescription }}</b></CFormLabel>
                                <CCol sm="3">
                                    <CFormSelect aria-label="select-selection" v-model="selectedSelection"
                                        feedback-invalid="Please, choose a selection operator" :invalid="selectedSelection == ''">
                                        <option selected disabled>Select the required selection</option>
                                        <option v-for="selection in selectionTypes" :key="selection" :value="selection.value">{{ selection.description }}</option>
                                    </CFormSelect>
                                </CCol>
                                <CCol sm="6">
                                    <CFormRange :min="0.05" :max="1" :step="0.05" v-model="selectedSelectionRate" />
                                </CCol>
                                <CCol sm="1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedSelectionRate }}</b></CFormLabel>
                                </CCol>
                            </CRow>
                            <!-- Tournament size (depending of the selection type) -->
                            <CRow class="mb-3" v-if="selectedSelection == 3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ tournamentSelectionRateDescription }}</b></CFormLabel>
                                <CCol sm="9">
                                    <CFormRange :min="0.05" :max="1" :step="0.05" v-model="selectedTournamentSelectionRate" />
                                </CCol>
                                <CCol sm="1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedTournamentSelectionRate }}</b></CFormLabel>
                                </CCol>
                            </CRow>
                            <!-- 
                        Crossover: type of crossover and prob 
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ crossoverDescription }}</b></CFormLabel>
                                <CCol sm="3">
                                    <CFormSelect aria-label="select-crossover" v-model="selectedCrossover"
                                        feedback-invalid="Please, choose a crossover operator" :invalid="selectedCrossover == ''">
                                        <option selected disabled>Select the required crossover</option>
                                        <option v-for="crossover in crossoverTypes" :key="crossover" :value="crossover.value">{{ crossover.description }}</option>
                                    </CFormSelect>
                                </CCol>
                                <CCol sm="6">
                                    <CFormRange :min="0" :max="1" :step="0.05" v-model="selectedProCrossover" />
                                </CCol>
                                <CCol sm="1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedProCrossover }}</b></CFormLabel>
                                </CCol>
                            </CRow>
                            <!-- 
                        Mutation: type of mutation and prob
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ mutationDescription }}</b></CFormLabel>
                                <CCol sm="3">
                                    <CFormSelect aria-label="select-mutation" v-model="selectedMutation"
                                        feedback-invalid="Please, choose a mutation operator" :invalid="selectedMutation == ''">
                                        <option selected disabled>Select the required mutation</option>
                                        <option v-for="mutation in mutationTypes" :key="mutation" :value="mutation.value">{{ mutation.description }}</option>
                                    </CFormSelect>
                                </CCol>
                                <CCol sm="6">
                                    <CFormRange :min="0" :max="1" :step="0.01" v-model="selectedProMutation" />
                                </CCol>
                                <CCol sm="1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedProMutation }}</b></CFormLabel>
                                </CCol>
                            </CRow>
                            <!--
                        Stop criterion
                    -->
                            <CRow class="mb-3">
                                <CFormLabel class="col-sm-2 col-form-label"><b>{{ stopCriterionDescription }}</b></CFormLabel>
                                <CCol sm="3">
                                    <!-- stop criterion type -->
                                    <CFormSelect aria-label="select-stop-criterion" v-model="selectedStopCriterion"
                                        feedback-invalid="Please select a stop criterion" 
                                        :invalid="selectedStopCriterion == ''" required>
                                        <option selected disabled>Select the required stop criterion</option>
                                        <option v-for="criterion in stopCriterionTypes" :key="criterion" :value="criterion.value">{{ criterion.description }}</option>
                                    </CFormSelect>
                                    <!-- <br /> -->
                                </CCol>
                                <CCol sm="1" v-if="selectedStopCriterion == 2">
                                    <CFormLabel>{{ effortDescription }}</CFormLabel>
                                </CCol>
                                <CCol sm="1" v-if="selectedStopCriterion == 1">
                                    <CFormLabel>{{ maxMiningTimeDescription }}</CFormLabel>
                                </CCol>
                                <CCol sm="5" v-if="selectedStopCriterion == 2">
                                    <CFormRange :min="0" :max="10000" :step="10" v-model="selectedEffort" />
                                </CCol>
                                <CCol sm="5" v-if="selectedStopCriterion == 1">
                                    <CFormRange :min=1 :max=60 :step="1" v-model="selectedMaxMiningTime" />
                                </CCol>
                                <CCol sm="1" v-if="selectedStopCriterion == 2">
                                    <CFormLabel class="col-form-label"><b>{{ selectedEffort }}</b></CFormLabel>
                                </CCol>
                                <CCol sm="1" v-if="selectedStopCriterion == 1">
                                    <CFormLabel class="col-form-label"><b>{{ selectedMaxMiningTime }}</b></CFormLabel>
                                </CCol>
                            </CRow>

                        </CCardBody>
                    </CCard>
                </div>

                <div v-if="selectedMod == 2 || selectedMod == 4">
                    <CCard>
                        <CCardTitle class="text-center">Entities assessment</CCardTitle>
                        <CCardBody>
                            <!-- 
                        Evaluator Mod 
                    -->
                            <div v-if="selectedMod == 4">
                                <!-- 
                            Probabilistic SHACL
                        -->
                                <CRow class="mb-3">
                                    <CFormLabel class="col-sm-2 col-form-label"><b>Probabilistic SHACL</b></CFormLabel>
                                    <CCol sm="2">
                                        <!-- Alpha -->
                                        <CFormLabel>{{ probShaclAlphaDescription }}</CFormLabel>
                                    </CCol>
                                    <CCol sm="2">
                                        <!-- Alpha -->
                                        <CFormSelect v-model="selectedProbShaclAlpha" required :disabled="selectedProbShaclP == 0">
                                            <option selected disabled>{{ probShaclAlphaDescription }}</option>
                                            <option v-for="alpha in probShaclAlphaTypes" :key="alpha" :value="alpha.value">{{ alpha.description }}</option>
                                        </CFormSelect>
                                    </CCol>
                                    <CCol sm="1">
                                        <!-- p-value -->
                                        <CFormLabel>{{ probShaclPDescription }}</CFormLabel>
                                    </CCol>
                                    <CCol sm="4">
                                        <CFormRange :min="0" :max="1" :step="0.05" v-model="selectedProbShaclP" />
                                    </CCol>
                                    <CCol sm="1">
                                        <CFormLabel class="col-form-label"><b>{{ selectedProbShaclP }}</b></CFormLabel>
                                    </CCol>
                                </CRow>
                                <CAlert color="warning" v-if="selectedProbShaclP == 0">
                                    <b>Standard SHACL validation !</b> increase the <b>P-value</b> to enable the <b>probabilistic SHACL validation</b>
                                </CAlert>
                                <CAlert color="warning" v-else>
                                    <b>Probabilistic SHACL validation !</b> set the <b>P-value</b> at 0 to enable the <b>standard SHACL validation</b>
                                </CAlert>
                                <!-- 
                            Shapes file 
                        -->
                                <CRow class="mb-3">
                                    <CFormLabel class="col-sm-2 col-form-label"><b>{{ shapesDescription }}</b></CFormLabel>
                                    <CCol sm="10">
                                        <input class="col-form-label" type="file" @change="readFile" />
                                        <!-- Content -->
                                        <CFormTextarea style="color: rgb(1, 108, 157)" v-model="selectedShapes">{{ selectedShapes }}</CFormTextarea>
                                    </CCol>
                                </CRow>
                            </div>
                            <div v-if="selectedMod.includes('af')">
                                <!-- 
                            OWL axioms file 
                        -->
                                <CRow class="mb-3">
                                    <CFormLabel class="col-sm-2 col-form-label"><b>{{ axiomsDescription }}</b></CFormLabel>
                                    <CCol sm="10">
                                        <input class="col-form-label" type="file" @change="readFile" />
                                        <!-- Content -->
                                        <CFormTextarea style="color: rgb(1, 108, 157)" v-model="selectedAxioms">{{ selectedAxioms }}</CFormTextarea>
                                    </CCol>
                                </CRow>
                            </div>
                        </CCardBody>
                    </CCard>
                </div>
                <!--
                    Launch XP
                -->
                <CButton color="success" @click="postProject">Let's go</CButton>
            </CForm>
        </CModalBody>
    </CModal>
</template>
  
<script>
// import LoginForm from '@/vues/auth/LoginForm.vue';
// https://coreui.io/vue/docs/components/modal.html
import {
    CModal, CModalHeader, CModalTitle, CModalBody, CCard, CCardBody, CCardTitle, 
    CForm, CRow, CFormLabel, CCol, CFormInput, CFormSelect, CFormTextarea, CFormRange, 
    CAlert, CButton
} from "@coreui/vue";
import { get, post } from "@/tools/api";

export default {
    name: 'CreateProject',
    components: {
        CModal, CModalHeader, CModalTitle, CModalBody, CCard, CCardBody, CCardTitle, 
        CForm, CRow, CFormLabel, CCol, CFormInput, CFormSelect, CFormTextarea, CFormRange, 
        CAlert, CButton
    },
    data() {
        return {
            validated: null,
            //
            errorMessage: "",
            projectToLoad: "",
            //
            // GENERAL SETTINGS
            //
            // projectName
            projectNameDescription                  : "",
            selectedProjectName                     : "",
            alreadyExist                            : true,
            // mod
            modDescription                          : "",
            modTypes                                : [],
            selectedMod                             : "",
            // prefixes
            prefixesDescription                     : "",
            prefixesTypes                           : [],
            selectedPrefixes                        : "",
            // Named RDF data graph
            namedDataGraphDescription               : "",
            namedDataGraphTypes                     : [],
            selectedNamedDataGraph                  : "",
            //
            // GRAMMATICAL EVOLUTION SETTINGS WITH DEFAULT VALUES
            //
            // BNF grammar
            grammarDescription                      : "",
            grammarTypes                            : [],
            filteredGrammarTypes                    : [],
            selectedGrammar                         : "",
            // population size 
            populationSizeDescription               : "",
            selectedPopulationSize                  : 0,
            // stop criterion
            stopCriterionDescription                : "",
            stopCriterionTypes                      : [],
            selectedStopCriterion                   : 0,
            // effort
            effortDescription                       : "",
            selectedEffort                          : 0,
            // clock-world time 
            maxMiningTimeDescription                : "",
            selectedMaxMiningTime                   : 0,
            // chromosome size
            sizeChromosomeDescription               : "",
            selectedSizeChromosome                  : 0,
            // max wrap
            maxWrapDescription                      : "",
            selectedMaxWrap                         : 0,
            // elite selection rate
            eliteSelectionRateDescription           : "",
            selectedEliteSelectionRate              : 0,
            // tournament selection rate
            tournamentSelectionRateDescription      : "",
            selectedTournamentSelectionRate         : 0,
            // selection type
            selectionDescription                    : "",
            selectionTypes                          : [],
            selectedSelection                       : 0,
            // selection rate
            selectionRateDescription                : "",
            selectedSelectionRate                   : 0,
            // crossover
            crossoverDescription                    : "",
            crossoverTypes                          : [],
            selectedCrossover                       : 0,
            // pro crossover
            proCrossoverDescription                 : "",
            selectedProCrossover                    : 0,
            // mutation
            mutationDescription                     : "",
            mutationTypes                           : [],
            selectedMutation                        : 0,
            // pro mutation
            proMutationDescription                  : "",
            selectedProMutation                     : 0,
            // SHACL: alpha and prob
            probShaclAlphaDescription               : "",
            probShaclAlphaTypes                     : [],
            selectedProbShaclAlpha                  : 0,
            //
            probShaclPDescription                   : "",
            selectedProbShaclP                      : 0,
            // Assessment: Axioms or SHACL
            axiomsDescription                       : "",
            selectedAxioms                          : 0,
            //
            shapesDescription                       : "",
            selectedShapes                          : 0,
        }
    },
    methods: {
        readFile(event) {
            const file = event.target.files[0];
            const reader = new FileReader();
            reader.onload = e => {
                if (this.selectedMod == 4) {
                    this.shapes = e.target.result;
                } else {
                    this.axioms = e.target.result;
                }
            }
            reader.readAsText(file);
        },
        getForm() {
            return {
                projectName                 : this.selectedProjectName,
                mod                         : this.selectedMod,
                prefixes                    : this.selectedPrefixes,
                namedDataGraph              : this.selectedNamedDataGraph,
                grammar                     : this.selectedGrammar,
                populationSize              : this.selectedPopulationSize,
                effort                      : this.selectedEffort,
                stopCriterion               : this.selectedStopCriterion,
                maxMiningTime               : this.selectedMaxMiningTime,
                sizeChromosome              : this.selectedSizeChromosome,
                maxWrap                     : this.selectedMaxWrap,
                eliteSelectionRate          : this.selectedEliteSelectionRate,
                tournamentSelectionRate     : this.selectedTournamentSelectionRate,
                selectionType               : this.selectedSelection,
                selectionRate               : this.selectedSelectionRate,
                mutationType                : this.selectedMutation,
                proMutation                 : this.selectedProMutation,
                crossoverType               : this.selectedCrossover,
                proCrossover                : this.selectedProCrossover,
                probShaclAlpha              : this.selectedProbShaclAlpha,
                probShaclP                  : this.selectedProbShaclP,
                axioms                      : this.selectedAxioms,
                shapes                      : this.selectedShapes
            }
        },
        async postProject() {
            const data = await post("api/project", {}, this.getForm());
            // if it has been pushed into the cluster
            if (data) {
                this.$store.commit('toggleCreateProjectPopup');
                this.$store.commit('toggleSuccessCreationPopupVisible', data);
                this.$store.commit('updateProjects');
            }
        },
        async getProject(pN) {
            return await get("api/project", { projectName: pN });
        },
        async setupParams() {
            const params = (await get("api/params", {}))[0];
            //
            // GENERAL SETTINGS
            //
            // projectName
            this.projectNameDescription                 = params.projectName.description;
            // mod
            this.modDescription                         = params.mod.description;
            this.modTypes                               = params.mod.values;
            // prefixes
            this.prefixesDescription                    = params.prefixes.description;
            this.prefixesTypes                          = params.prefixes.values;
            // Named RDF data graph
            this.namedDataGraphDescription              = params.namedDataGraph.description;
            this.namedDataGraphTypes                    = params.namedDataGraph.values;
            //
            // GRAMMATICAL EVOLUTION SETTINGS WITH DEFAULT VALUES
            //
            // BNF grammar
            this.grammarDescription                     = params.grammar.description;
            this.grammarTypes                           = params.grammar.values;
            // population size 
            this.populationSizeDescription              = params.populationSize.description;
            this.selectedPopulationSize                 = params.populationSize.values[0].value;
            // stop criterion
            this.stopCriterionDescription               = params.stopCriterion.description;
            this.stopCriterionTypes                     = params.stopCriterion.values;
            this.selectedStopCriterion                  = params.stopCriterion.values[0].value;
            // effort
            this.effortDescription                      = params.effort.description;
            this.selectedEffort                         = params.effort.values[0].value;
            // clock-world time 
            this.maxMiningTimeDescription               = params.maxMiningTime.description;
            this.selectedMaxMiningTime                  = params.maxMiningTime.values[0].value;
            // chromosome size
            this.sizeChromosomeDescription              = params.sizeChromosome.description;
            this.selectedSizeChromosome                 = params.sizeChromosome.values[0].value;
            // max wrap
            this.maxWrapDescription                     = params.maxWrap.description;
            this.selectedMaxWrap                        = params.maxWrap.values[0].value;
            // elite selection rate
            this.eliteSelectionRateDescription          = params.eliteSelectionRate.description;
            this.selectedEliteSelectionRate             = params.eliteSelectionRate.values[0].value;
            // tournament selection rate
            this.tournamentSelectionRateDescription     = params.tournamentSelectionRate.description;
            this.selectedTournamentSelectionRate        = params.tournamentSelectionRate.values[0].value;
            // selection type
            this.selectionDescription                   = params.selectionType.description;
            this.selectionTypes                         = params.selectionType.values;
            this.selectedSelection                      = params.selectionType.values[0].value;
            // selection rate
            this.selectionRateDescription               = params.selectionRate.description;
            this.selectedSelectionRate                  = params.selectionRate.values[0].value;
            // crossover
            this.crossoverDescription                   = params.crossoverType.description;
            this.crossoverTypes                         = params.crossoverType.values;
            this.selectedCrossover                      = params.crossoverType.values[0].value;
            // pro crossover
            this.proCrossoverDescription                = params.proCrossover.description;
            this.selectedProCrossover                   = params.proCrossover.values[0].value;
            // mutation
            this.mutationDescription                    = params.mutationType.description;
            this.mutationTypes                          = params.mutationType.values;
            this.selectedMutation                       = params.mutationType.values[0].value;
            // pro mutation
            this.proMutationDescription                 = params.proMutation.description;
            this.selectedProMutation                    = params.proMutation.values[0].value;
            // SHACL: alpha and prob
            this.probShaclAlphaDescription              = params.probShaclAlpha.description;
            this.probShaclAlphaTypes                    = params.probShaclAlpha.values;
            this.selectedProbShaclAlpha                 = params.probShaclAlpha.values[0].value;
            //
            this.probShaclPDescription                  = params.probShaclP.description;
            this.selectedProbShaclP                     = params.probShaclP.values[0].value;
            // Assessment: Axioms or SHACL
            this.axiomsDescription                      = params.axioms.description;
            this.selectedAxioms                         = params.axioms.values[0].value;
            //
            this.shapesDescription                      = params.shapes.description;
            this.selectedShapes                         = params.shapes.values[0].value;
        }
    },
    watch: {
        async projectToLoad() {
            const project = (await this.getProject(this.projectToLoad))[0];
            this.selectedMod                        = project.mod;
            this.selectedPrefixes                   = project.prefixes;
            this.selectedNamedDataGraph             = project.namedDataGraph;
            this.selectedGrammar                    = project.grammar;
            this.selectedPopulationSize             = project.populationSize;
            this.selectedEffort                     = project.effort;
            this.selectedStopCriterion              = project.stopCriterionType;
            this.selectedMaxMiningTime              = project.maxMiningTime;
            this.selectedSizeChromosome             = project.sizeChromosome;
            this.selectedMaxWrap                    = project.maxWrap;
            this.selectedEliteSelectionRate         = project.eliteSelectionRate;
            this.selectedTournamentSelectionRate    = project.tournamentSelectionRate;
            this.selectedSelection                  = project.selectionType;
            this.selectedSelectionRate              = project.selectionRate;
            this.selectedMutation                   = project.mutationType;
            this.selectedProMutation                = project.proMutation;
            this.selectedCrossover                  = project.crossoverType;
            this.selectedProCrossover               = project.proCrossover;
            this.selectedProbShaclAlpha             = project.probShaclAlpha;
            this.selectedProbShaclP                 = project.probShaclP;
            this.selectedAxioms                     = project.axioms;
            this.selectedShapes                     = project.shapes;
        },
        selectedMod() {
            if (this.selectedMod == 3) {
                // filtering BNF templates with grammars related to SHACL shapes
                this.filteredGrammarTypes = this.grammarTypes.filter((grammar) => {
                    return grammar.description.includes("SHACL") == true;
                });
            } else if (this.selectedMod == 1) {
                // filtering BNF templates with grammars related to SHACL shapes
                this.filteredGrammarTypes = this.grammarTypes.filter((grammar) => {
                    return grammar.description.includes("OWL") == true;
                });
            }
        },
        async selectedProjectName() {
            if (this.selectedProjectName == '') {
                this.errorMessage = "Please, choose a name for your project";
            } else if ((await this.getProject(this.selectedProjectName)).length != 0) {
                this.alreadyExist = true;
                this.errorMessage = "This project name already exists !"
            } else {
                this.alreadyExist = false;
            }
        }
    },
    mounted() {
        this.setupParams();
    },
}
</script>