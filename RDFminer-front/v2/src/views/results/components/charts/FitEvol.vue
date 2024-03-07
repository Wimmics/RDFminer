<template>
    <CCard>
        <CCardTitle class="text-center"><b>Fitness</b> evolution</CCardTitle>
        <CCardBody>
            <CChart type="line" :wrapper="true" :data="fitnessScoreData" :options="options"
                :key="this.$store.state.selectedResults" :redraw="true">
            </CChart>
        </CCardBody>
    </CCard>
</template>

<script>
import { CChart } from '@coreui/vue-chartjs'
import { CCard, CCardBody, CCardTitle } from '@coreui/vue';

export default {
    name: 'FitEvol',
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
            options: {},
        };
    },
    mounted() {
        this.options = {
            // maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    type: 'logarithmic',
                    min: 10e-10,
                    ticks: {
                        callback: function (value) {
                            // round values
                            return Number(value.toFixed(10));
                        },
                    },
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
        // Individuals fitness chart
        fitnessScoreData() {
            return {
                labels: Array.from({ length: this.$store.state.fitnessScoreData.length }, (_, i) => i + 1),
                datasets: [
                    {
                        label: 'Mean',
                        backgroundColor: 'rgba(0, 255, 3, 0.2)',
                        borderColor: 'rgba(0, 178, 2, 0.8)',
                        pointBackgroundColor: 'rgba(0, 255, 3, 0.2)',
                        pointBorderColor: 'rgba(0, 148, 2, 0.8)',
                        borderWidth: '3',
                        data: this.$store.state.fitnessScoreData
                    },
                    {
                        label: 'Median',
                        backgroundColor: 'rgba(255, 199, 0, 0.1)',
                        borderColor: 'rgba(255, 199, 0, 0.6)',
                        pointBackgroundColor: 'rgba(255, 199, 0, 1)',
                        pointBorderColor: 'rgba(255, 199, 0, 1)',
                        borderDash: [5, 5],
                        tension: 0.1,
                        fill: "+1",
                        data: this.$store.state.medianFitnessScoreData,
                    },
                    {
                        label: 'Max',
                        backgroundColor: 'rgba(255, 0, 0, 0.1)',
                        borderColor: 'rgba(255, 0, 0, 0.6)',
                        pointBackgroundColor: 'rgba(255, 0, 0, 0.6)',
                        borderDash: [5, 5],
                        tension: 0.1,
                        data: this.$store.state.maxFitnessScoreData,
                    },

                ],
            }
        },
    },
}
</script>