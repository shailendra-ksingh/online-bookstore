import axios from "axios";

// Central Axios configuration used for all backend API requests.
const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    headers: {
        "Content-Type": "application/json"
    },
    timeout: 10000
});

export default apiClient;