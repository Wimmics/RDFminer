<template>
    <CRow>
        <CCol sm="12">
            <CCard>
                <!-- <CCardImage width="10" height="20" orientation="top" src="../assets/fitness.png"></CCardImage> -->
                <CCardTitle class="text-center"><b>Population</b> evolution</CCardTitle>
                <CCardBody>
                    <CChart type="line" :wrapper="true" :data="populationEvolutionData" :options="populationEvolutionOptions" :key="this.$store.state.selectedResults"
                        :redraw="true"></CChart>
                </CCardBody>
            </CCard>
        </CCol>
    </CRow>
    <br />
    <CRow>
        <CCol sm="6">
            <CCard>
                <!-- <CCardImage width="10" height="20" orientation="top" src="../assets/fitness.png"></CCardImage> -->
                <CCardTitle class="text-center">Individuals with <b>non-null fitness</b></CCardTitle>
                <CCardBody>
                    <CChart type="bar" :wrapper="true" :data="nonNullIndividualData" :options="options" :key="this.$store.state.selectedResults"
                        :redraw="true">
                    </CChart>
                </CCardBody>
            </CCard>
        </CCol>
        <CCol sm="6">
            <CCard>
                <!-- <CCardImage width="10" height="20" orientation="top" src="../assets/fitness.png"></CCardImage> -->
                <CCardTitle class="text-center"><b>Fitness</b> global score</CCardTitle>
                <CCardBody>
                    <CChart type="line" :wrapper="true" :data="fitnessScoreData" :options="options" :key="this.$store.state.selectedResults"
                        :redraw="true">
                    </CChart>
                </CCardBody>
            </CCard>
        </CCol>
    </CRow>
</template>

<script>
// https://developers.google.com/chart/interactive/docs/gallery/areachart?hl=fr#overview
// import { GChart } from 'vue-google-charts'
// import { entities } from '../data/results_1.json'
import { CChart } from '@coreui/vue-chartjs'
import { CCard, CCardBody, CCardTitle, CRow, CCol } from '@coreui/vue';
// import io from "socket.io-client";
// import { socket } from '@/tools/env';

export default {
    name: 'Charts',
    components: {
        CChart, CCard, CCardBody, CCardTitle, CRow, CCol
    },
    data() {
        return {
            // socket io
            // generations
            // nGenerations: 0,
            // n_gen labels
            // options plugin
            plugins: {
                legend: {
                    display: true,
                    labels: {
                        font: {
                            size: 16,
                        }
                    }
                },
                tooltip: {
                    callbacks: {
                        title: function (context) {
                            return "Generation " + context[0].label;
                        },
                        label: function (context) {
                            return context.formattedValue;
                        },
                    }
                }
            },
            pluginsDisplayPercent: {
                legend: {
                    display: true,
                    labels: {
                        font: {
                            size: 16,
                        }
                    }
                },
                tooltip: {
                    callbacks: {
                        title: function (context) {
                            return "Generation " + context[0].label;
                        },
                        label: function (context) {
                            // return context.dataset.label + " value: " + context.formattedValue;
                            return (context.formattedValue * 100) + "%";
                        },
                    }
                }
            },
            // chart options
            options: {},
            populationEvolutionOptions: {},
        };
    },
    mounted() {
        this.options = {
            // maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                },
                x: {
                    beginAtZero: false,
                    type: 'linear',
                    title: {
                        display: true,
                        text: 'Generation',
                        font: {
                            size: 18
                        }
                    },
                }
            },
            plugins: this.plugins
        };
        this.populationEvolutionOptions = {
            // maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                },
                x: {
                    beginAtZero: false,
                    type: 'linear',
                    title: {
                        display: true,
                        text: 'Generation',
                        font: {
                            size: 18
                        }
                    },
                }
            },
            plugins: this.pluginsDisplayPercent
        };
    },
    computed: {
        // Individuals with non-null fitness chart
        nonNullIndividualData() {
            return { 
                labels: Array.from({ length: this.$store.state.nonNullIndividualData.length }, (_, i) => i + 1),
                datasets: [
                    {
                        label: 'Individuals with non-null fitness',
                        backgroundColor: 'rgba(222, 0, 0, 0.8)',
                        data: this.$store.state.nonNullIndividualData
                    }
                ],
            };
        },
        // Individuals fitness chart
        fitnessScoreData() {
            return {
                labels: Array.from({ length: this.$store.state.fitnessScoreData.length }, (_, i) => i + 1),
                datasets: [
                    {
                        label: 'Individuals fitness evolution',
                        backgroundColor: 'rgba(0, 255, 3, 0.8)',
                        borderColor: 'rgba(0, 178, 2, 0.8)',
                        pointBackgroundColor: 'rgba(0, 255, 3, 0.8)',
                        pointBorderColor: 'rgba(0, 148, 2, 0.8)',
                        data: this.$store.state.fitnessScoreData
                    }
                ],
            }
        },
        // Pop evol chart
        populationEvolutionData() {
            return {
                labels: Array.from({ length: this.$store.state.populationDevelopmentRateData.length }, (_, i) => i + 1),
                datasets: [
                    {
                        label: 'Population development rate',
                        backgroundColor: 'rgba(0, 119, 216, 0.8)',
                        borderColor: 'rgba(0, 119, 216, 0.8)',
                        pointBackgroundColor: 'rgba(0, 146, 255, 0.8)',
                        pointBorderColor: 'rgba(78, 176, 255, 0.8)',
                        data: this.$store.state.populationDevelopmentRateData
                    },
                    {
                        label: 'Diversity coefficient',
                        backgroundColor: 'rgba(255, 173, 0, 0.8)',
                        borderColor: 'rgba(255, 173, 0, 0.8)',
                        pointBackgroundColor: 'rgba(255, 211, 118, 0.8)',
                        pointBorderColor: 'rgba(202, 137, 0, 0.8)',
                        data: this.$store.state.diversityCoefficientData
                    }
                ],
            }
        },
    },
}
</script>