<template>
    <div style="display: flex; flex-direction: column; align-items: center; text-align: center;">
        <!-- <CIcon v-if="checked" icon="cil-check" title="toot" size="xxl" /> -->
        <CImage v-if="checked" :src="check" width="40" height="40" />
        <CSpinner v-else color="success" />
        <br />
        {{ message }}
    </div>
</template>

<script>
// https://developers.google.com/chart/interactive/docs/gallery/areachart?hl=fr#overview
// import { entities } from '../data/results_1.json'
// CProgress, CProgressBar
import { CImage, CSpinner } from '@coreui/vue';
import check from '@/assets/images/check.png'
// import axios from 'axios';
// import io from "socket.io-client";

export default {
    name: 'Item',
    components: {
        CImage, CSpinner //CProgress, CProgressBar, 
    },
    props: {
        message: String,
        checked: Boolean,
    },
    data() {
        return {
            check
        }
    }
}
</script>
