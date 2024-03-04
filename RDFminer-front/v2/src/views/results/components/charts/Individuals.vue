<template>
    <CCard>
        <CCardTitle class="text-center"><b>Population</b> evolution</CCardTitle>
        <CCardBody>
            <CChart type="bubble" :wrapper="true" :data="individualsData" :options="options"
                :key="this.$store.state.selectedResults" :redraw="true">
            </CChart>
        </CCardBody>
    </CCard>
</template>

<script>
import { CChart } from '@coreui/vue-chartjs'
import { CCard, CCardBody, CCardTitle } from '@coreui/vue';

export default {
    name: 'Individuals',
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
                        // title: function (context) {
                        //     return "Generation " + context[0].label;
                        // },
                        label: function (context) {
                            // return context.dataset.label + " value: " + context.formattedValue;
                            // return "toto: " + context.parsed.y;
                            let elite = context.parsed._custom > 5 ? "[elite] " : "";
                            return elite + ((1 - context.parsed.y) * 100).toFixed(2) + "% of confirmations (support: " + context.parsed.x + ")"
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
                    // beginAtZero: true
                    title: {
                        display: true,
                        text: '% violations',
                        font: {
                            size: 18
                        }
                    }
                },
                x: {
                    beginAtZero: true,
                    // type: 'linear',
                    title: {
                        display: true,
                        text: 'Reference cardinality',
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
        individualsData() {
            return {
                labels: Array.from({ length: this.$store.state.populationDevelopmentRateData.length }, (_, i) => i + 1),
                datasets: [
                    {
                        label: 'Individuals',
                        pointBackgroundColor: function (context) {
                            return 'rgba(' + Math.round(255 * context.raw.y) + ', ' + Math.round(255 * (1 - context.raw.y)) + ', 0, 1)';
                        },
                        data: this.$store.state.individualsData
                    },
                ],
            }
        },
    },
}
</script>