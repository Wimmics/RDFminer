<template>
  <router-view />
  <!-- Popups -->
  <Login></Login>
  <DetailsPopup></DetailsPopup>
  <CreateProject></CreateProject>
  <SuccessCreation></SuccessCreation>
  <DeleteProject></DeleteProject>
  <Register></Register>
</template>

<script>
import Login from '@/views/popup/Login.vue';
import DetailsPopup from '@/views/popup/Details.vue';
import CreateProject from './views/popup/CreateProject.vue';
import SuccessCreation from './views/popup/SuccessCreation.vue';
import DeleteProject from './views/popup/DeleteProject.vue';
import Register from './views/popup/Register.vue';

export default {
  name: 'App',
  components: {
    Login, DetailsPopup, CreateProject, SuccessCreation, DeleteProject,
    Register
},
}
</script>

<style lang="scss">
// Import Main styles for this application
@import 'styles/style';
</style>
