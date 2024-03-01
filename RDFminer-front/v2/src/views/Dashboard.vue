<template>
  <div>
    <h2 v-if="$store.state.isAuth">Hello {{ username }}, nice to see you !</h2>
    <h2 v-else>Welcome to the RDFminer App ! Please, <a class="clickable-link" @click="$store.commit('toggleLoginPopupVisible')">log in</a> or <a class="clickable-link" @click="$store.commit('toggleRegisterPopupVisible')">create an account</a> to use its features !</h2>
    <!-- <WidgetsStatsA /> -->
    <br />
    <CRow>
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
    <CRow>
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
import { useCookies } from "vue3-cookies"

export default {
  name: 'Dashboard',
  components: {
    Projects,
    Results
  },
  setup() {
    const username = useCookies(["username"]).cookies.get("username");
    return {
      username,
    }
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