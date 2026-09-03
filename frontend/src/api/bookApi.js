import apiClient from "./apiClient";

// Book-related API calls.
export const getBooks = async () => {
    const response = await apiClient.get("/books");
    return response.data;
};