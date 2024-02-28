export default [
  {
    component: 'CNavTitle',
    name: 'PAGES',
  },
  {
    component: 'CNavItem',
    name: 'Dashboard',
    to: '/dashboard',
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
]
