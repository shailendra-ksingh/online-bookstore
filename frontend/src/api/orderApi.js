import apiClient from "./apiClient";

// Order-related API calls.
export const createOrder = async () => {
    const response = await apiClient.post("/orders");
    return response.data;
};