<template>
    <CCard>
        <CCardTitle class="text-center"><b>Population</b> evolution</CCardTitle>
        <CCardBody>
            <CChart type="line" :wrapper="true" :data="populationEvolutionData" :options="options"
                :key="this.$store.state.selectedResults" :redraw="true">
            </CChart>
        </CCardBody>
    </CCard>
</template>

<script>
import { CChart } from '@coreui/vue-chartjs'
import { CCard, CCardBody, CCardTitle } from '@coreui/vue';

export default {
    name: 'PopEvol',
    components: {
        CChart, CCard, CCardBody, CCardTitle
    },
    data() {
        return {
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
                            // return context.dataset.label + " value: " + context.formattedValue;
                            return (context.formattedValue * 100) + "%";
                        },
                    }
                }
            },
            // chart options
            options: {},
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
                    ticks: {
                        stepSize: 1,
                    }
                }
            },
            plugins: this.plugins
        };
    },
    computed: {
        // Pop evol chart
        populationEvolutionData() {
            return {
                labels: Array.from({ length: this.$store.state.populationDevelopmentRateData.length }, (_, i) => i + 1),
                datasets: [
                    {
                        label: 'Population development rate',
                        backgroundColor: 'rgba(0, 119, 216, 0.2)',
                        borderColor: 'rgba(0, 119, 216, 0.8)',
                        pointBackgroundColor: 'rgba(0, 146, 255, 0.8)',
                        pointBorderColor: 'rgba(78, 176, 255, 0.8)',
                        fill: 'origin',
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