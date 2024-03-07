<template>
    <CCard class="card text-center">
        <!-- <CCardImage width="10" height="20" orientation="top" src="../assets/fitness.png"></CCardImage> -->
        <CCardTitle><b>Overview</b> of extracted entities</CCardTitle>
        <CCardBody>
            <!-- <CChart type="bubble" :wrapper="true" :data="bubble_chart" :redraw="true" height="75"></CChart> -->
            <GChart class="chart" type="BubbleChart" :data="data"
                :options="options" :key="refresh"/>
            <!-- <CSpinner v-else color="primary">Waiting the end of execution ...</CSpinner> -->
        </CCardBody>
    </CCard>
</template>

<script>
// https://developers.google.com/chart/interactive/docs/gallery/areachart?hl=fr#overview
import { GChart } from 'vue-google-charts'
import { CCard, CCardBody, CCardTitle } from '@coreui/vue';
import { toRaw } from 'vue';
import { get } from '@/tools/api';
import { bubbleOptions, headers } from '../settings/bubble';
// import io from "socket.io-client";
import { socket } from '@/tools/env';

export default {
    name: 'BubbleEntities',
    components: {
        GChart, CCard, CCardBody, CCardTitle
    },
    props: {
        results: {
            type: Object
        }
    },
    data() {
        return {
            // force refresh of component
            refresh: true,
            // socket io
            socket: socket,
            // CoreUI CCharts: Bubble chart
            // bubble_chart: {},
            // ELAPSED TIME / REFERENCE CARDINALITY (EXCEPTIONS ?)
            data: [],
            options: bubbleOptions,
        };
    },
    mounted() {
        // console.log(this.results);
        this.data.push(headers);
        // iterate on entities found
        for (let i = 0; i < toRaw(this.results.entities.length); i++) {
            // console.log(entity.numExceptions / entity.referenceCardinality)
            this.data.push([
                toRaw(this.results.entities[i].phenotype),
                toRaw(this.results.entities[i].numExceptions),
                toRaw(this.results.entities[i].elapsedTime),
                toRaw(this.results.entities[i].numExceptions) / toRaw(this.results.entities[i].referenceCardinality),
                toRaw(this.results.entities[i].referenceCardinality)
            ]);
        }
        // SOCKET IO
        this.socket.on("update-results", (id) => {
            console.log("socket.io - update-results - " + id);
            if (this.results._id == id) {
                this.getData(id);
            }
        });
    },
    methods: {
        async getData(id) {
            // reset data
            this.data.pop();
            this.data.push(headers);
            // entities data
            const updated = await get("api/results", { resultsID: id });
            // iterate on entities found
            for (let i = 0; i < toRaw(updated.entities.length); i++) {
                // console.log(entity.numExceptions / entity.referenceCardinality)
                this.data.push([
                    toRaw(updated.entities[i].phenotype),
                    toRaw(updated.entities[i].numExceptions),
                    toRaw(updated.entities[i].elapsedTime),
                    toRaw(updated.entities[i].numExceptions) / toRaw(updated.entities[i].referenceCardinality),
                    toRaw(updated.entities[i].referenceCardinality)
                ]);
            }
            this.refresh = !this.refresh;
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.card {
    width: 100%;
    height: 70vh;
}

.chart {
    display: flex;
    justify-content: space-around;
    width: 100%;
    height: 100%;
}

@media screen and (max-width: 768px) {
    .chart {
        flex-direction: column;
        align-items: center;
    }
}
</style>