<template>
  <CSidebar
    position="fixed"
    :visible="sidebarVisible"
    @visible-change="
      (event) =>
        $store.commit({
          type: 'updateSidebarVisible',
          value: event,
        })
    "
  >
    <CSidebarBrand>
      <!-- <CIcon
        custom-class-name="sidebar-brand-full"
        :icon="logo"
        :height="35"
      /> -->
      <!-- <img class="sidebar-brand-full" height="35" alt="Vue logo" :src="logo">
      <h5>RDFminer UI - v1.0</h5> -->
      <div class="justify-content-start align-items-center">
        <img :src="logo" alt="Logo" height="110"/>
        <h5>RDFminer UI</h5>
      </div>
    </CSidebarBrand>
    <AppSidebarNav />
    <!-- <CSidebarToggler
      class="d-none d-lg-flex"
      @click="$store.commit('toggleUnfoldable')"
    /> -->
  </CSidebar>
</template>

<script>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { AppSidebarNav } from './AppSidebarNav'
// import { logoNegative } from '@/assets/brand/logo-negative'
// import { sygnet } from '@/assets/brand/sygnet'
import logo from '@/assets/brand/rdfminer-sidebar.jpg';

export default {
  name: 'AppSidebar',
  components: {
    AppSidebarNav,
  },
  setup() {
    const store = useStore()
    return {
      logo,
      sidebarUnfoldable: computed(() => store.state.sidebarUnfoldable),
      sidebarVisible: computed(() => store.state.sidebarVisible),
    }
  },
}
</script>
