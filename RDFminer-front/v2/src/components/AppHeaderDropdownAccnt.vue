<template>
  <CDropdown variant="nav-item">
    <CDropdownToggle placement="bottom-end" class="py-0" :caret="false">
      <CAvatar :src="$store.state.isAuth ? logged : notLogged " size="md" />
    </CDropdownToggle>
    <CDropdownMenu class="pt-0">
      <CDropdownItem> {{ $store.state.userName }} </CDropdownItem>
      <CDropdownHeader component="h6" class="bg-light fw-semibold py-2">Account</CDropdownHeader>
      <div v-if="!$store.state.isAuth">
        <CDropdownItem  @click="$store.commit('toggleLoginPopupVisible')"> <CIcon icon="cil-lock-unlocked"/> Login   </CDropdownItem>
        <CDropdownItem  @click="$store.commit('toggleRegisterPopupVisible')"> <CIcon icon="cil-laptop"/>     Register   </CDropdownItem>
      </div>
      <CDropdownItem v-if="$store.state.isAuth"  @click="logout()">        <CIcon icon="cil-lock-locked"/>   Logout  </CDropdownItem>
    </CDropdownMenu>
  </CDropdown>
</template>

<script>
import logged from '@/assets/images/avatars/profil.svg'
import notLogged from '@/assets/images/avatars/not-logged.svg'

export default {
  name: 'AppHeaderDropdownAccnt',
  setup() {
    return {
      logged: logged,
      notLogged: notLogged,
    }
  },
  methods: {
    logout() {
      this.$store.commit("logout");
      this.$cookies.remove("token");
      this.$cookies.remove("username");
    }
  }
  // data() {
  //   return {
  //     userName: "",
  //     isAuth: false,
  //   }
  // },
}
</script>
