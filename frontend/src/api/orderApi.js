import apiClient from "./apiClient";

export const createOrder = async () => {
    const response = await apiClient.post("/orders");
    return response.data;
};