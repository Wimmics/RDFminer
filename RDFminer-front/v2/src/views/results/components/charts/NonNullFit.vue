<template>
    <CCard>
        <!-- <CCardImage width="10" height="20" orientation="top" src="../assets/fitness.png"></CCardImage> -->
        <CCardTitle class="text-center">Individuals with <b>non-null fitness</b></CCardTitle>
        <CCardBody>
            <CChart type="bar" :wrapper="true" :data="nonNullIndividualData" :options="options"
                :key="this.$store.state.selectedResults" :redraw="true">
            </CChart>
        </CCardBody>
    </CCard>
</template>

<script>
import { CChart } from '@coreui/vue-chartjs'
import { CCard, CCardBody, CCardTitle } from '@coreui/vue';

export default {
    name: 'NonNullFit',
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
                            return context.formattedValue;
                        },
                    }
                }
            },
            // chart options
            options: {}
        };
    },
    mounted() {
        this.options = {
            // maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1,
                    }
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
        // Individuals with non-null fitness chart
        nonNullIndividualData() {
            return {
                labels: Array.from({ length: this.$store.state.nonNullIndividualData.length }, (_, i) => i + 1),
                datasets: [
                    {
                        label: 'Cardinality',
                        backgroundColor: 'rgba(222, 0, 0, 0.8)',
                        data: this.$store.state.nonNullIndividualData
                    }
                ],
            };
        }
    },
}
</script>