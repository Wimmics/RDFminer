<template>
    <CModal :visible="$store.state.successCreationPopupVisible" alignment="center" @close="$store.commit('toggleSuccessCreationPopupVisible', {})" scrollable>
        <CModalHeader>
            <CModalTitle><b>{{ $store.state.selectedProject.projectName }}</b> has been created !</CModalTitle>
        </CModalHeader>
        <CModalBody>
            <!-- USERNAME -->
            <CRow class="mb-3">
                This project has been created and save it in the queue, 
                this project will be automatically launched as soon as the server is available.
            </CRow>
        </CModalBody>
    </CModal>
</template>
  
<script>
// import LoginForm from '@/vues/auth/LoginForm.vue';
// https://coreui.io/vue/docs/components/modal.html
import { CRow, CModal, CModalHeader, CModalTitle, CModalBody } from "@coreui/vue";

export default {
    name: 'SuccessCreation',
    components: {
        CRow, CModal, CModalHeader, CModalTitle, CModalBody
    },
}
</script>
  
<style scoped></style>