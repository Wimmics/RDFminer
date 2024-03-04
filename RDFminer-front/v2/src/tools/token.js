import { useCookies } from "vue3-cookies";

export function getToken() { 
    if (useCookies(["token"]).cookies.get("token") == null)
        return "";
    return useCookies(["token"]).cookies.get("token"); 
}

export function getUsername() {
    if (useCookies(["username"]).cookies.get("username") == null)
        return "";
    return useCookies(["username"]).cookies.get("username");
}