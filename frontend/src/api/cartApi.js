import apiClient from "./apiClient";

export const getCart = async () => {
    const response = await apiClient.get("/cart");
    return response.data;
};

export const addBookToCart = async (bookId, quantity = 1) => {
    const response = await apiClient.post("/cart", {
        bookId,
        quantity
    });

    return response.data;
};

export const updateCartQuantity = async (bookId, quantity) => {
    const response = await apiClient.put(`/cart/${bookId}`, {
        quantity
    });

    return response.data;
};

export const removeCartItem = async (bookId) => {
    const response = await apiClient.delete(`/cart/${bookId}`);
    return response.data;
};