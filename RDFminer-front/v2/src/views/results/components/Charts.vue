<template>
    <CRow>
        <CCol sm="6">
            <PopEvol></PopEvol>
        </CCol>
        <CCol sm="6">
            <Individuals></Individuals>
        </CCol>
    </CRow>
    <br />
    <CRow>
        <CCol sm="6">
            <NonNullFit></NonNullFit>
        </CCol>
        <CCol sm="6">
            <FitEvol></FitEvol>
        </CCol>
    </CRow>
</template>

<script>
// https://developers.google.com/chart/interactive/docs/gallery/areachart?hl=fr#overview
// import { GChart } from 'vue-google-charts'
// import { entities } from '../data/results_1.json'
import { CRow, CCol } from '@coreui/vue';
import FitEvol from '@/views/results/components/charts/FitEvol.vue';
import PopEvol from '@/views/results/components/charts/PopEvol.vue';
import NonNullFit from '@/views/results/components/charts/NonNullFit.vue';
import Individuals from '@/views/results/components/charts/Individuals.vue';
// import io from "socket.io-client";
// import { socket } from '@/tools/env';

export default {
    name: 'Charts',
    components: {
        CRow, CCol, FitEvol, PopEvol, NonNullFit, Individuals
    },
}
</script>