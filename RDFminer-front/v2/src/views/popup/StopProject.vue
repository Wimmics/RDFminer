<template>
    <CModal :visible="$store.state.stopProjectPopupVisible" alignment="center"
        @close="$store.commit('toggleStopProjectPopupVisible', $store.state.selectedProject)" scrollable>
        <CModalHeader>
            <CModalTitle>Stop <b>{{ $store.state.selectedProject.projectName }}</b> project</CModalTitle>
        </CModalHeader>
        <CModalBody>
            <!-- USERNAME -->
            <CRow class="mb-3">
                Are you sure to stop the project ?
            </CRow>
            <!-- ALERT -->
            <CAlert :visible="$store.state.isStopped" color="success" class="d-flex align-items-center">
                <!-- <CIcon class="flex-shrink-0 me-2" width="24" height="24" /> -->
                <div>
                    The project <b>{{ $store.state.selectedProject.projectName }}</b> has been stopped !
                </div>
            </CAlert>
            <!-- <CAlert :visible="isError" color="danger" class="d-flex align-items-center">
                <div>
                    Error during deletion ({{ code }}), please try again later...
                </div>
            </CAlert> -->
        </CModalBody>
        <CModalFooter>
            <CButton color="success" @click="$store.commit('stopProject')" :visible="!$store.state.waitingStop">Yes</CButton>
            <!-- ALERT -->
            <CAlert :visible="$store.state.waitingStop" color="waring" class="d-flex align-items-center">
                <CSpinner size="sm" /> Stopping the project...
            </CAlert>
        </CModalFooter>
    </CModal>
</template>

<script>
// import LoginForm from '@/vues/auth/LoginForm.vue';
// https://coreui.io/vue/docs/components/modal.html
import { CButton, CRow, CModal, CModalHeader, CModalTitle, CModalBody, CModalFooter, CAlert } from "@coreui/vue";


export default {
    name: 'StopProject',
    components: {
        CButton, CRow, CModal, CModalHeader, CModalTitle, CModalBody, CModalFooter, CAlert
    },
}
</script>

<style scoped></style>