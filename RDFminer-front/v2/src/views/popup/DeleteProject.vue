<template>
    <CModal :visible="$store.state.deleteProjectPopupVisible" alignment="center" @close="$store.commit('toggleDeleteProjectPopupVisible', {})" scrollable>
        <CModalHeader>
            <CModalTitle>Delete <b>{{ $store.state.selectedProject.projectName }}</b> project</CModalTitle>
        </CModalHeader>
        <CModalBody>
            <!-- USERNAME -->
            <CRow class="mb-3">
                Are you sure to delete the project ?
            </CRow>
            <!-- ALERT -->
            <CAlert :visible="$store.state.isDeleted" color="success" class="d-flex align-items-center">
                <!-- <CIcon class="flex-shrink-0 me-2" width="24" height="24" /> -->
                <div>
                    The project <b>{{ $store.state.selectedProject.projectName }}</b> has been deleted !
                </div>
            </CAlert>
            <!-- <CAlert :visible="isError" color="danger" class="d-flex align-items-center">
                <div>
                    Error during deletion ({{ code }}), please try again later...
                </div>
            </CAlert> -->
        </CModalBody>
        <CModalFooter v-if="!$store.state.isDeleted">
            <CButton color="success" @click="$store.commit('deleteProject')">Yes</CButton>
        </CModalFooter>
    </CModal>
</template>
  
<script>
// import LoginForm from '@/vues/auth/LoginForm.vue';
// https://coreui.io/vue/docs/components/modal.html
import { CButton, CRow, CModal, CModalHeader, CModalTitle, CModalBody, CModalFooter, CAlert } from "@coreui/vue";


export default {
    name: 'DeleteProject',
    components: {
        CButton, CRow, CModal, CModalHeader, CModalTitle, CModalBody, CModalFooter, CAlert
    },
}
</script>
  
<style scoped></style>