<template>
  <div>
    <AppSidebar />
    <div class="wrapper d-flex flex-column min-vh-100 bg-light">
      <AppHeader />
      <div class="body flex-grow-1 px-3" style="padding-bottom: 60px;">
        <CContainer lg>
          <router-view />
        </CContainer>
      </div>
      <AppFooter class="fixed-footer d-md-down-none me-auto mb-0"/>
    </div>
  </div>
</template>
<script>
import { CContainer } from '@coreui/vue'
import AppFooter from '@/components/AppFooter.vue'
import AppHeader from '@/components/AppHeader.vue'
import AppSidebar from '@/components/AppSidebar.vue'

export default {
  name: 'DefaultLayout',
  components: {
    AppFooter,
    AppHeader,
    AppSidebar,
    CContainer,
  },
}
</script>

<style scoped>
/* Fixed Footer Style */
.fixed-footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  background-color: #f8f9fa; /* Customize background color */
  padding: 10px; /* Customize padding */
}

.container-lg {
  max-width: 1500px;
}
</style>