<template>
  <router-view />
  <Login></Login>
  <DetailsPopup></DetailsPopup>
  <CreateProject></CreateProject>
  <SuccessCreation></SuccessCreation>
</template>

<script>
import Login from '@/views/popup/Login.vue';
import DetailsPopup from '@/views/popup/Details.vue';
import CreateProject from './views/popup/CreateProject.vue';
import SuccessCreation from './views/popup/SuccessCreation.vue';

export default {
  name: 'App',
  components: {
    Login, DetailsPopup, CreateProject, SuccessCreation
  },
}
</script>

<style lang="scss">
// Import Main styles for this application
@import 'styles/style';
</style>
