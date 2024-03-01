import { createStore } from 'vuex'
import { get, del } from "@/tools/api";
import { getToken } from '@/tools/token';

export const crypto = require("crypto-js");

export default createStore({
  state: {
    // user informations
    isAuth: getToken() != "",
    projects: getToken() != "" ? await get("api/projects/", {}) : [],
    result: {},
    // login popup
    loginPopupVisible: false,
    // register popup
    registerPopupVisible: false,
    // project details popup
    detailsPopupVisible: false,
    selectedProject: {},
    // create project popup 
    createProjectPopupVisible: false,
    successCreationPopupVisible: false,
    // delete project 
    deleteProjectPopupVisible: false,
    isDeleted: false,
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
    toggleRegisterPopupVisible(state) {
      state.registerPopupVisible = !state.registerPopupVisible;
    },
    async login(state) {
      state.isAuth = true;
      state.projects = await get("api/projects/", {});
    },
    logout(state) {
      state.isAuth = false;
      state.projects = [];
      state.result = {};
      state.selectedResults = {};
    },
    //
    // Projects
    //
    async updateProjects(state) {
      state.projects = await get("api/projects/", {});
    },
    async updateSelectedProject(state, id) {
      state.selectedProject = (await get("api/project", { id: id }))[0];
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
    toggleDeleteProjectPopupVisible(state, project) {
      state.selectedProject = project;
      state.isDeleted = false;
      state.deleteProjectPopupVisible = !state.deleteProjectPopupVisible;
    },
    async deleteProject(state) {
      state.isDeleted = await del("api/project", { projectName: state.selectedProject.projectName });
      state.projects = await get("api/projects/", {});
      state.selectedProject = {};
      state.selectedResults = {};
    },
    //
    // Results
    // 
    async updateSelectedResults(state, project) {
      state.selectedProject = (await get("api/project", { projectName: project.projectName }))[0];
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
    },
    downloadShapes(state) {
      // we must preprocess project and results fields to build well-formed SHACL shapes file
      // fetching prefixes
      let shapes = "# Shapes file extracted from results obtained\n# Project: " + state.selectedProject.projectName + "\n" 
      shapes += state.selectedProject.prefixes + "\n#\n";
      // iterate on shapes founded
      state.selectedResults.entities.forEach((entity) => {
        // the subjects of shapes are missing in the results !  
        // we used the phenotype to generate a hashcode with ':' as subject of each shapes, e.g. ':5s4dha4483 [...]'
        let subject = ':' + crypto.SHA1(entity.phenotype).toString();
        // concat subject with the rest of phenotype
        shapes += subject + ' ' + entity.phenotype + "\n";
      });
      const blob = new Blob([shapes], { type: "application/octet-stream" });
      // download link
      const url = URL.createObjectURL(blob);
      // create <a>
      const link = document.createElement("a");
      link.href = url;
      link.download = "shapes.ttl";
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
