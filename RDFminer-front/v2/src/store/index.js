import { createStore } from 'vuex'
import { get } from "@/tools/api";
import { getToken } from '@/tools/token';

export default createStore({
  state: {
    // user informations
    isAuth: getToken() != "",
    projects: getToken() != "" ? await get("api/projects/", {}) : [],
    result: {},
    // login popup
    loginPopupVisible: false,
    // project details popup
    detailsPopupVisible: false,
    selectedProject: {},
    // create project popup 
    createProjectPopupVisible: false,
    successCreationPopupVisible: false,
    // selected results 
    selectedResults: {},
    // kpi results 
    nonNullIndividualData: [],
    fitnessScoreData: [],
    populationDevelopmentRateData: [],
    diversityCoefficientData: [],
    // sidebar
    sidebarVisible: '',
  },
  mutations: {
    //
    // Sidebar
    //
    toggleSidebar(state) {
      state.sidebarVisible = !state.sidebarVisible;
    },
    updateSidebarVisible(state, payload) {
      state.sidebarVisible = payload.value;
    },
    // 
    // Authentification
    //
    toggleLoginPopupVisible(state) {
      state.loginPopupVisible = !state.loginPopupVisible;
    },
    updateIsAuth(state) {
      state.isAuth = !state.isAuth;
    },
    async login(state) {
      state.isAuth = true;
      state.projects = await get("api/projects/", {});
    },
    logout(state) {
      state.isAuth = false;
      state.projects = [];
      state.result = {};
    },
    //
    // Projects
    //
    async updateProjects(state) {
      state.projects = await get("api/projects/", {});
    },
    toggleDetailsPopup(state, project) {
      state.detailsPopupVisible = !state.detailsPopupVisible;
      state.selectedProject = project;
    },
    toggleCreateProjectPopup(state) {
      state.createProjectPopupVisible = !state.createProjectPopupVisible;
    },
    toggleSuccessCreationPopupVisible(state, project) {
      if (project != {}) {
        state.selectedProject = project;
      }
      state.successCreationPopupVisible = !state.successCreationPopupVisible;
    },
    //
    // Results
    // 
    async updateSelectedResults(state, project) {
      state.selectedProject = project;
      state.selectedResults = await get("api/results", { resultsID: project.resultsID });
      state.nonNullIndividualData = [];
      state.fitnessScoreData = [];
      state.populationDevelopmentRateData = [];
      state.diversityCoefficientData = [];
      for (let i = 0; i < state.selectedResults.generations.length; i++) {
        state.nonNullIndividualData.push(state.selectedResults.generations[i].numIndividualsWithNonNullFitness);
        state.fitnessScoreData.push(state.selectedResults.generations[i].averageFitness);
        state.populationDevelopmentRateData.push(state.selectedResults.generations[i].populationDevelopmentRate);
        state.diversityCoefficientData.push(state.selectedResults.generations[i].diversityCoefficient);
      }
    },
    downloadResults(state) {
      const blob = new Blob([JSON.stringify(state.selectedResults, null, "\t")], { type: "application/octet-stream" });
      // download link
      const url = URL.createObjectURL(blob);
      // create <a>
      const link = document.createElement("a");
      link.href = url;
      link.download = "results.json";
      // trigger it
      document.body.appendChild(link);
      link.click();
      // Clean elements
      URL.revokeObjectURL(url);
      document.body.removeChild(link);
    }
  },
  actions: {

  },
  getters: {

  },
  modules: {

  },
})
