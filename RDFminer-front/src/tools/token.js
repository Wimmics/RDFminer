import { useCookies } from "vue3-cookies";

export function getToken() { 
    if (useCookies(["token"]).cookies.get("token") == null)
        return "";
    return useCookies(["token"]).cookies.get("token"); 
}