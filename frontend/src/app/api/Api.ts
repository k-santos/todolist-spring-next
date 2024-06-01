import axios from "axios";
import { parseCookies } from "nookies";

const { "app.token": token } = parseCookies();

const api = axios.create({
  baseURL: "http://localhost:8080",
});

if (token) {
  api.defaults.headers["Authorization"] = `Bearer ${token}`;
}

export default api;
