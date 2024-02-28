<template>
    <div>
        <CCard>
            <CCardBody>
                <!-- <CCardHeader>PAPER TYPE</CCardHeader> -->
                <CCardTitle>Rémi Felin</CCardTitle>
                <CCardSubtitle class="mb-2 text-muted">PhD student</CCardSubtitle>
                <CCardSubtitle class="mb-2 text-muted">Université Côte d'Azur</CCardSubtitle>
                <!-- <CCardSubtitle v-if="paper.distinction.link != ''">
                    <a :href="paper.distinction.link">
                        {{ paper.distinction.type }}
                    </a>
                </CCardSubtitle> -->
                <!-- <CCardText><b>Abstract: </b>{{ paper.abstract }}</CCardText> -->
                <CCardLink :href="'mailto:remi.felin@inria.fr'">Send an email</CCardLink>
                <!-- <CCardLink :href="paper.link">Access to the ressource</CCardLink> -->
            </CCardBody>
        </CCard>
        <br>
    </div>
</template>


<script>
// import { publications } from '../data/publications.json'
// import _ from 'lodash';
// import { get } from '@/tools/api';
import { CCard, CCardBody, CCardTitle, CCardSubtitle, CCardLink } from '@coreui/vue'
// import { useCookies } from 'vue3-cookies'

export default {
    name: 'Contacts',
    components: {
        CCard, CCardBody, CCardTitle, CCardSubtitle, CCardLink
    },
}
</script>