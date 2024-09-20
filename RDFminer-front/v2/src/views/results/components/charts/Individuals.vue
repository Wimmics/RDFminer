<template>
    <CCard class="text-center">
        <CCardTitle>Characteristics of the entities</CCardTitle>
        <CRow class="align-items-center">
            <!-- <CCard class="text-center">
                    <CCardTitle></CCardTitle>
                    <CCardBody>
                        <CRow> -->
            <!-- Color legend -->
            <CCol xs="3"><b>%confirmation</b></CCol>
            <CCol xs="1" class="text-center" style="font-size: smaller;">0%</CCol>
            <CCol xs="3">
                <div class="legend-box"></div>
            </CCol>
            <CCol xs="1" class="text-center" style="font-size: smaller;">100%</CCol>
            <!-- Size legend -->
            <CCol xs="2"><b>elite ?</b></CCol>
            <CCol xs="1" class="elite">
                <div class="big-bubble"></div>
                <div style="font-size: smaller;">Yes</div>
            </CCol>
            <!-- <CCol xs="1" class="text-center" style="font-size: smaller;"></CCol> -->
            <CCol xs="1" class="elite">
                <div class="small-bubble"></div>
                <div style="font-size: smaller;">No</div>
            </CCol>
            <!-- <CCol xs="1" class="text-center" style="font-size: smaller;"></CCol> -->
            <!-- </CRow>
                    </CCardBody>
                </CCard> -->
        </CRow>
        <CCardBody>
            <CChart type="bubble" :wrapper="true" :data="individualsData" :options="options"
                :key="this.$store.state.selectedResults" :redraw="true">
            </CChart>
        </CCardBody>
    </CCard>
</template>

<script>
// import { CChart } from '@coreui/vue-chartjs'
// import { CCard, CCardBody, CCardTitle } from '@coreui/vue';

export default {
    name: 'Individuals',
    // components: {
    //     CChart//, CCard, CCardBody, CCardTitle
    // },
    data() {
        return {
            plugins: {
                legend: {
                    display: false,
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
                datasets: [
                    {
                        label: 'Individuals',
                        pointBackgroundColor: function (context) {
                            if (context.raw.y === null)
                                return 'rgba(0,0,0,0)';
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

<style>
.legend-box {
    /* width: 200px; */
    height: 20px;
    /* vertical-align: baseline; */
    background: linear-gradient(to right, red, rgb(0, 255, 0));
}

.small-bubble {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background-color: rgb(255, 0, 0);
}

.big-bubble {
    width: 15px;
    height: 15px;
    border-radius: 50%;
    background-color: rgb(255, 0, 0);
}

.elite {
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>