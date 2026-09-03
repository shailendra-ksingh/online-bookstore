import axios from "axios";

// Central Axios configuration used for all backend API requests.
const apiBaseUrl = import.meta.env.VITE_API_URL;

if (!apiBaseUrl) {
    throw new Error(
        "VITE_API_URL environment variable is not configured"
    );
}

const apiClient = axios.create({
    baseURL: apiBaseUrl,
    headers: {
        "Content-Type": "application/json"
    },
    timeout: 10000
});

export default apiClient;