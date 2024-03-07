import { createStore } from 'vuex'
import { get, del, put } from "@/tools/api";
import { getToken, getUsername } from '@/tools/token';
import { median } from '@/tools/tools';

export const crypto = require("crypto-js");

export default createStore({
  state: {
    // user informations
    isAuth: getToken() != "",
    username: getUsername(),
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
    // stop project 
    stopProjectPopupVisible: false,
    waitingStop: false,
    stoppedProject: {},
    // selected results 
    selectedResults: {},
    // kpi results 
    individualsData: [],
    nonNullIndividualData: [],
    populationDevelopmentRateData: [],
    diversityCoefficientData: [],
    // fitness 
    fitnessScoreData: [],
    minFitnessScoreData: [],
    maxFitnessScoreData: [],
    medianFitnessScoreData: [],
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
    isSelectedProject(state, project) {
      return state.selectedProject.projectName == project.projectName;
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
    toggleStopProjectPopupVisible(state, project) {
      state.selectedProject = project;
      state.isStopped = false;
      state.stopProjectPopupVisible = !state.stopProjectPopupVisible;
    },
    async stopProject(state) {
      state.waitingStop = true;
      let projectToStop = await put("api/project", {}, { projectName: state.selectedProject.projectName });
      if (projectToStop != null) {
        state.selectedProject = projectToStop;
      }
      state.waitingStop = false;
      state.stopProjectPopupVisible = false;
      state.projects = await get("api/projects/", {});
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
      // population statistics
      state.nonNullIndividualData = [];
      state.populationDevelopmentRateData = [];
      state.diversityCoefficientData = [];
      state.fitnessScoreData = [];
      state.minFitnessScoreData = [];
      state.maxFitnessScoreData = [];
      state.medianFitnessScoreData = [];
      for (let i = 0; i < state.selectedResults.generations.length; i++) {
        state.nonNullIndividualData.push(state.selectedResults.generations[i].numIndividualsWithNonNullFitness);
        state.populationDevelopmentRateData.push(state.selectedResults.generations[i].populationDevelopmentRate);
        state.diversityCoefficientData.push(state.selectedResults.generations[i].diversityCoefficient);
        state.fitnessScoreData.push(state.selectedResults.generations[i].averageFitness);
        state.minFitnessScoreData.push(Math.min(...state.selectedResults.generations[i].fitnesses));
        state.maxFitnessScoreData.push(Math.max(...state.selectedResults.generations[i].fitnesses));
        state.medianFitnessScoreData.push(median(state.selectedResults.generations[i].fitnesses));
      }
      // individuals
      state.individualsData = [];
      for (let i = 0; i < state.selectedResults.entities.length; i++) {
        // size must be between 0 and 100 
        // let size = state.selectedResults.entities[i].numConfirmations / state.selectedResults.entities[i].referenceCardinality;
        // violations rate for 'y' value
        let rate = state.selectedResults.entities[i].numExceptions / state.selectedResults.entities[i].referenceCardinality;
        // elite individuals will be bigger !
        let size = state.selectedResults.entities[i].elite ? 8 : 4;
        state.individualsData.push({
          x: state.selectedResults.entities[i].referenceCardinality,
          y: rate,
          r: size
        });
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
