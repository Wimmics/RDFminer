<template>
    <CButton color="success" @click="$store.commit('toggleCreateProjectPopup')" :disabled="!$store.state.isAuth">Create a new project</CButton>
    <br />
    <br />
    <CTable align="middle" class="mb-0 border" hover responsive striped>
        <CTableHead color="light">
            <CTableRow>
                <CTableHeaderCell v-for="header in headers" :key="header" scope="col">{{ header }}</CTableHeaderCell>
            </CTableRow>
        </CTableHead>
        <CTableBody>
            <CTableRow v-for="project in $store.state.projects" :key="project" align="middle">
                <CTableHeaderCell scope="row">{{ project.projectName }}</CTableHeaderCell>
                <!-- <CTableDataCell>{{ project.mod }}</CTableDataCell> -->
                <!-- <CTableDataCell><a :href="project.targetSparqlEndpoint">{{ project.targetSparqlEndpoint }}</a> -->
                <!-- </CTableDataCell> -->
                <CTableDataCell>
                    <CButton color="primary" variant="outline" style="margin:5px;" @click="$store.commit('toggleDetailsPopup', project)">
                        <CIcon icon="cil-equalizer" />Details
                    </CButton>
                </CTableDataCell>
                <CTableDataCell>
                    <!-- <CAvatar class="clickable" src="assets/dashboard.png" @click="redirectVisu(project.projectName)"
                    v-if="project.status != 0" /> -->
                    <CButton v-if="project.status != 0" @click="$store.commit('updateSelectedResults', project)" color="info"
                        variant="outline" :active="$store.state.selectedProject.projectName == project.projectName">
                        <CIcon icon="cil-graph" />Results
                    </CButton>
                    <CButton disabled v-else>
                        <CSpinner variant="grow" size="sm" aria-hidden="true" />
                        Waiting for the server
                    </CButton>
                </CTableDataCell>
                <CTableDataCell>
                    <!-- <CButton color="success" variant="outline" :disabled="project.status != 0" style="margin:5px;">
                    <CImage src="assets/start.png" width="20" height="20" />
                    Start
                </CButton> -->
                    <CButton color="danger" variant="outline" style="margin:5px;" @click="$store.commit('toggleStopProjectPopupVisible', project)" :disabled="project.status != 1">
                        <CIcon icon="cil-media-stop" />Stop
                    </CButton>
                    <CButton color="danger" style="margin:5px;" @click="$store.commit('toggleDeleteProjectPopupVisible', project)">
                        <CIcon icon="cil-delete" />Delete
                    </CButton>
                    <!-- <CAvatar class="clickable" src="assets/cancel.png" />
                <CAvatar class="clickable" src="assets/dashboard.png" @click="redirectVisu(project.projectName)"
                    v-if="project.status != 0" />
                <CAvatar class="clickable" src="assets/garbage.png" @click="deletePopup(project.projectName)" /> -->
                </CTableDataCell>
                <CTableDataCell :color="getColor(project.status)" style="font-weight: bold;">
                    <CSpinner :variant="project.status < 1 ? 'grow' : 'border'" v-if="project.status != 2" size="sm"
                        style="margin-right:10px;" />{{
                            status[project.status].text }}
                </CTableDataCell>
            </CTableRow>
        </CTableBody>
        <CTableFoot>
        </CTableFoot>
    </CTable>
</template>


<script>
import { CTable, CTableHead, CTableBody, CTableFoot, CTableRow, CTableHeaderCell, CTableDataCell, CSpinner, CButton, CIcon } from '@coreui/vue';
// import { useCookies } from 'vue3-cookies';
// import { get } from "@/tools/api";
import { socket } from "@/tools/env";

export default {
    name: 'Projects',
    components: {
        CTable, CTableHead, CTableBody, CTableFoot, CTableRow,
        CTableHeaderCell, CTableDataCell, CSpinner, CButton,
        CIcon
    },
    methods: {
        getColor(status) {
            switch (status) {
                default:
                case 0:
                    return "light";
                case 1:
                    return "warning";
                case 2:
                    return "success";
                case -1:
                    return "danger";
            }
        },
    },
    data() {
        return {
            // cookies: useCookies(["token", "id"]).cookies,
            socket: socket,
            status: {
                0: { text: "Pending...", color: "red" },
                1: { text: "In progress", color: "orange" },
                2: { text: "Complete", color: "green" }
            },
            showDeletePopup: false,
            selectedProject: "",
            headers: ["Name", "Parameters", "Results", "Operations", "Status"],
            item: null,
        };
    },
    mounted() {
        // this.cookies = useCookies(["token", "id"]).cookies;
        // console.log("Token: " + this.cookies.get("token"));
        // SOCKET IO
        // update project status
        this.socket.on("update-status", (id) => {
            if (this.$store.state.selectedProject._id == id) {
                console.log("[socket-io] update-status for project.id(" + id + ")");
                this.$store.commit('updateSelectedProject', id);
                this.$store.commit('updateProjects');
            }
        });
        // SOCKET IO
        this.socket.on("update-results", (id) => {
            if (this.$store.state.selectedResults._id == id) {
                console.log("[socket-io] update-results: " + id);
                this.$store.commit('updateSelectedResults', this.$store.state.selectedProject);
            }
        });
    }
}
</script>