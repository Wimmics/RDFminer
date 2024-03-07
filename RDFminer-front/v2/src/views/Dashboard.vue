<template>
  <div>
    <h2 v-if="$store.state.isAuth">Hello {{ $store.state.username }}, nice to see you !</h2>
    <h2 v-else style="text-align: center;">Welcome to the RDFminer App ! Please, <a class="clickable-link" @click="$store.commit('toggleLoginPopupVisible')">log in</a> or <a class="clickable-link" @click="$store.commit('toggleRegisterPopupVisible')">create an account</a> !</h2>
    <!-- <WidgetsStatsA /> -->
    <br />
    <CRow v-if="$store.state.isAuth">
      <CCol :md="12">
        <CCard class="mb-4">
          <CCardHeader>
            <h4>Projects</h4>
          </CCardHeader>
          <CCardBody>
            <Projects></Projects>
          </CCardBody>
        </CCard>
      </CCol>
    </CRow>
    <CRow v-if="$store.state.isAuth">
      <CCol :md="12">
        <CCard class="mb-4">
          <CCardHeader>  
            <h4>Results</h4>
            <div class="text-medium-emphasis small">Project: {{ $store.state.selectedProject.projectName }}</div>
          </CCardHeader>
          <CCardBody>
            <Results></Results>
          </CCardBody>
        </CCard>
      </CCol>
    </CRow>
  </div>
</template>

<script>
import Projects from '@/views/projects/Projects.vue'
import Results from '@/views/results/Results.vue'

export default {
  name: 'Dashboard',
  components: {
    Projects,
    Results
  },
}
</script>

<style scoped>
.clickable-link {
  color: rgb(0, 0, 134);
  cursor: pointer;
  text-decoration: underline;
}
</style>