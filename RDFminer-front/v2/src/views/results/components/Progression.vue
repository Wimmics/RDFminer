<template>
    <CRow>
        <CCol sm="1">
            <Item 
                :message="'Launching'" 
                :checked="$store.state.selectedProject.status > 0"></Item>
        </CCol>
        <CCol sm="2" style="display: flex; justify-content: center; flex-direction: column;">
            <CProgress>
                <CProgressBar 
                    :color="$store.state.selectedProject.status > 0 ? 'success' : 'warning'" 
                    :value="100" 
                    :variant="$store.state.selectedProject.status > 0 ? '': 'striped'" 
                    animated/>
            </CProgress>
        </CCol>
        <CCol sm="2">
            <Item 
                :message="'Fetching data from RDF graph'" 
                :checked="!($store.state.selectedProject.status == 1 && $store.state.selectedResults.generations.length == 0)"></Item>
        </CCol>
        <CCol sm="2" style="display: flex; justify-content: center; flex-direction: column;">
            <CProgress>
                <CProgressBar 
                    :color="$store.state.selectedProject.status == 1 && $store.state.selectedResults.generations.length == 0 ? 'warning' : 'success'" 
                    :value="100" 
                    :variant="$store.state.selectedProject.status == 1 && $store.state.selectedResults.generations.length == 0 ? 'striped': ''" 
                    animated/>
            </CProgress>
        </CCol>
        <CCol sm="2">
            <Item 
                :message="'Performing GE (generation ' + $store.state.selectedResults.generations.length + ')'" 
                :checked="$store.state.selectedProject.status == 2"></Item>
        </CCol>
        <CCol sm="2" style="display: flex; justify-content: center; flex-direction: column;">
            <CProgress>
                <CProgressBar 
                    :color="$store.state.selectedProject.status == 2 ? 'success' : 'warning'" 
                    :value="100" 
                    :variant="$store.state.selectedProject.status == 2 ? '': 'striped'" 
                    animated/>
            </CProgress>
        </CCol>
        <CCol sm="1">
            <Item :message="'Finishing'" :checked="$store.state.selectedProject.status == 2"></Item>
        </CCol>
    </CRow>
</template>

<script>
// https://developers.google.com/chart/interactive/docs/gallery/areachart?hl=fr#overview
// import { entities } from '../data/results_1.json'
// CProgress, CProgressBar
import { CCol, CRow, CProgress, CProgressBar } from '@coreui/vue';
import Item from './Item.vue';
// import axios from 'axios';
// import io from "socket.io-client";

export default {
    name: 'Progression',
    components: {
        CCol, CRow, Item, CProgress, CProgressBar
    }
}
</script>
