<template>
    <div>
        <!-- Specific search by key -->
        <!-- <label style="font-size: 1vw;">Filtering results: </label>
        <select style="font-size: 1vw;" v-model="choosenFilter">
            <option value="">--Please choose an option--</option>
            <option value="date_asc">By date (asc)</option>
            <option value="date_desc">By date (desc)</option>
            <option value="title">By title</option>
            <option value="conferenceTitle">By conference</option>
            <option value="place">By place</option>
        </select> -->
        <div v-for="paper in papers" :key="paper">
            <CCard>
                <CCardBody>
                    <!-- <CCardHeader>PAPER TYPE</CCardHeader> -->
                    <CCardTitle>{{ paper.title }}</CCardTitle>
                    <CCardSubtitle class="mb-2 text-muted">
                        <template v-for="(author, idx) in paper.authors" :key="idx">
                            {{ author }} <template v-if="idx != paper.authors.length - 1"> and </template>
                        </template>
                    </CCardSubtitle>
                    <CCardSubtitle class="mb-2 text-muted">{{ paper.conferenceTitle }} ({{ paper.date }})</CCardSubtitle>
                    <CCardSubtitle v-if="paper.distinction.link != ''">
                        <a :href="paper.distinction.link">
                            {{ paper.distinction.type }}
                        </a>
                    </CCardSubtitle>
                    <CCardText><b>Abstract: </b>{{ paper.abstract }}</CCardText>
                    <CCardText>
                        <b>Keywords: </b>
                        <template v-for="(keyword, idx) in paper.keywords" :key="idx">
                            {{ keyword }} <template v-if="idx != paper.keywords.length - 1"> ; </template>
                        </template>
                    </CCardText>
                    <CCardLink @click="copyCitation(paper.citation)">Copy Citation</CCardLink>
                    <CCardLink :href="paper.link">Access to the ressource</CCardLink>
                </CCardBody>
            </CCard>
            <br>
        </div>
    </div>
</template>


<script>
// import { publications } from '../data/publications.json'
// import _ from 'lodash';
import { get } from '@/tools/api';
import { CCard, CCardBody, CCardText, CCardTitle, CCardSubtitle, CCardLink } from '@coreui/vue'
// import { useCookies } from 'vue3-cookies'

export default {
    name: 'Publications',
    components: {
        CCard, CCardBody, CCardText, CCardTitle, CCardSubtitle, CCardLink
    },
    data() {
        return {
            papers: [],
            keywords: [],
            choosenFilter: "",
        }
    },
    mounted() {
        this.getPublications();
    },
    methods: {
        async getPublications() {
            // get project
            const publications = await get("api/publications", {});
            // console.log(project);
            this.papers = publications;
        },
        async copyCitation(citation) {
            try {
                await navigator.clipboard.writeText(citation);
                alert('Copied');
            } catch ($e) {
                alert('Cannot copy');
            }
        },
    }
}
</script>