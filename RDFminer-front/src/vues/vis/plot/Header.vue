<template>
    <CRow>
        <CCol sm="4">
            <CCard class="card">
                <!-- <CCardImage width="10" height="20" orientation="top" src="../assets/fitness.png"></CCardImage> -->
                <CCardTitle class="text-center">Generations computation time (in ms.)</CCardTitle>
                <CCardBody>
                    <CChart type="bar" :wrapper="true" :data="computationTimeChart" :options="options" :key="refresh"
                        :redraw="true">
                    </CChart>
                </CCardBody>
            </CCard>
        </CCol>
        <CCol sm="8">
            <CCard class="card-progression">
                <!-- <CCardImage width="10" height="20" orientation="top" src="../assets/fitness.png"></CCardImage> -->
                <CCardBody>
                    <CCardTitle class="text-center"><b>Progression</b></CCardTitle>
                    <CProgress class="mb-3" height="50">
                        <CProgressBar :value="progression" color="success" :variant="progression == 100 ? '' : 'striped'"
                            animated><b style="font-size: large;">{{ progression }}%</b></CProgressBar>
                    </CProgress>
                    <CCardTitle class="text-center"><b>Actions</b></CCardTitle>
                    <!-- <CButton color="primary" variant="outline" @click="showDetails">
                        Check project settings</CButton> -->
                    <CButton style="margin: 5px;" color="success" variant="outline" :disabled="progression != 100"
                        @click="getResults">Download results (JSON)</CButton>
                    <!-- <CButton color="info" variant="outline" :disabled="progression != 100" @click="getSHACLReport">
                        Download SHACL report (Turtle)</CButton> -->
                </CCardBody>
            </CCard>
        </CCol>
    </CRow>
    <!-- Details Popup -->
    <DetailsPopup :enable="showDetailsPopup" :data="project"></DetailsPopup>
</template>

<script>
// https://developers.google.com/chart/interactive/docs/gallery/areachart?hl=fr#overview
// import { entities } from '../data/results_1.json'
import { CChart } from '@coreui/vue-chartjs';
import { CCard, CCardBody, CCardTitle, CCol, CProgress, CProgressBar, CButton, CRow } from '@coreui/vue';
import { toRaw } from 'vue';
import { options } from '../settings/GE';
import { get } from '@/tools/api';
import DetailsPopup from '../../projects/popup/DetailsPopup.vue';
// import axios from 'axios';
// import io from "socket.io-client";
import { socket } from '@/tools/env';

export default {
    name: 'VHeader',
    components: {
        CChart, CCard, CCardBody, CCardTitle, CCol, CProgress, CProgressBar, CButton,
        CRow, DetailsPopup
    },
    props: {
        results: {
            type: Object
        },
    },
    data() {
        return {
            // force refresh of component
            refresh: true,
            showDetailsPopup: false,
            // socket io
            socket: socket,
            // data
            id: this.results._id,
            generations: this.results.generations,
            entities: this.results.entities,
            // x-axis labels
            labels: [],
            // chart options
            options: options,
            // CoreUI CCharts: Individuals with non-null fitness
            computationTimeData: [],
            computationTimeChart: {},
        };
    },
    mounted() {
        // deduce x-labels
        this.labels = Array.from({ length: toRaw(this.generations.length) }, (_, idx) => idx + 1);
        for (let i = 0; i < toRaw(this.generations.length); i++) {
            this.computationTimeData[i] = toRaw(this.generations[i].computationTime);
        }
        // Individuals with non-null fitness chart
        this.computationTimeChart = {
            labels: this.labels,
            datasets: [
                {
                    label: 'Computation time',
                    backgroundColor: 'rgba(36, 168, 178, 0.8)',
                    data: this.computationTimeData
                }
            ],
        };
        // SOCKET IO
        this.socket.on("update-results", (data) => {
            this.generations = data.generations;
            this.entities = data.entities;
            // refresh
            this.refresh = !this.refresh;
        });
        // }
    },
    methods: {
        showDetails() {
            this.showDetailsPopup = !this.showDetailsPopup;
        },
        async getResults() {
            const results = await get("api/results", { resultsID: this.id });
            this.download(results, "results.json");
        },
        download(data, name) {
            const blob = new Blob([JSON.stringify(data, null, "\t")], { type: "application/octet-stream" });
            // download link
            const url = URL.createObjectURL(blob);
            // create <a>
            const link = document.createElement("a");
            link.href = url;
            link.download = name;
            // trigger it
            document.body.appendChild(link);
            link.click();
            // Clean elements
            URL.revokeObjectURL(url);
            document.body.removeChild(link);
        }
    },
    // watch: {
    //     curGeneration() {
    //         this.progression = (this.curGeneration / this.nGenerations) * 100;
    //     }
    // }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.card {
    height: 100%;
    width: 100%;
}
</style>