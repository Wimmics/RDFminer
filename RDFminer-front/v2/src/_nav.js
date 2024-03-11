export default [
  {
    component: 'CNavTitle',
    name: 'PAGES',
  },
  {
    component: 'CNavItem',
    name: 'Dashboard',
    to: '/',
    icon: 'cil-speedometer',
    // badge: {
    //   color: 'primary',
    //   text: 'COOL',
    // },
  },
  {
    component: 'CNavItem',
    name: 'Publications',
    to: '/publications',
    icon: 'cil-notes',
  },
  {
    component: 'CNavItem',
    name: 'Contacts',
    to: '/contacts',
    icon: 'cil-pencil',
  },
  {
    component: 'CNavItem',
    name: 'Tutorial',
    to: '/tutorial',
    icon: 'cilScreenDesktop'
  }
]
