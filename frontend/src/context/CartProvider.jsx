import { useCallback, useEffect, useState } from "react";
import { CartContext } from "./CartContext";

import {
    getCart,
    addBookToCart,
    updateCartQuantity,
    removeCartItem
} from "../api/cartApi";


export function CartProvider({ children }) {

    const [cartItems, setCartItems] = useState([]);
    const [cartTotal, setCartTotal] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");


    const updateCartState = (cart) => {

        setCartItems(cart.items || []);
        setCartTotal(Number(cart.total || 0));
    };


    const loadCart = useCallback(async () => {

        setLoading(true);
        setError("");

        try {

            const cart = await getCart();

            updateCartState(cart);

        } catch (error) {

            console.error("Failed to load cart", error);
            setError("Unable to load cart.");

        } finally {

            setLoading(false);
        }

    }, []);


    useEffect(() => {

        void loadCart();

    }, [loadCart]);


    const addToCart = async (book) => {

        setLoading(true);
        setError("");

        try {

            const cart = await addBookToCart(book.id, 1);

            updateCartState(cart);

        } catch (error) {

            console.error("Failed to add book to cart", error);
            setError("Unable to add book to cart.");

        } finally {

            setLoading(false);
        }
    };


    const increaseQuantity = async (bookId) => {

        const item = cartItems.find(
            (cartItem) => cartItem.bookId === bookId
        );

        if (!item || loading) {
            return;
        }

        setLoading(true);
        setError("");

        try {

            const cart = await updateCartQuantity(
                bookId,
                item.quantity + 1
            );

            updateCartState(cart);

        } catch (error) {

            console.error("Failed to increase quantity", error);
            setError("Unable to update cart quantity.");

        } finally {

            setLoading(false);
        }
    };


    const decreaseQuantity = async (bookId) => {

        const item = cartItems.find(
            (cartItem) => cartItem.bookId === bookId
        );

        if (!item || item.quantity <= 1 || loading) {
            return;
        }

        setLoading(true);
        setError("");

        try {

            const cart = await updateCartQuantity(
                bookId,
                item.quantity - 1
            );

            updateCartState(cart);

        } catch (error) {

            console.error("Failed to decrease quantity", error);
            setError("Unable to update cart quantity.");

        } finally {

            setLoading(false);
        }
    };


    const removeFromCart = async (bookId) => {

        if (loading) {
            return;
        }

        setLoading(true);
        setError("");

        try {

            const cart = await removeCartItem(bookId);

            updateCartState(cart);

        } catch (error) {

            console.error("Failed to remove cart item", error);
            setError("Unable to remove item from cart.");

        } finally {

            setLoading(false);
        }
    };


    const getCartItemCount = () => {

        return cartItems.reduce(
            (total, item) => total + item.quantity,
            0
        );
    };


    const value = {

        cartItems,
        cartTotal,
        loading,
        error,

        loadCart,
        addToCart,
        increaseQuantity,
        decreaseQuantity,
        removeFromCart,

        getCartItemCount
    };


    return (
        <CartContext.Provider value={value}>
            {children}
        </CartContext.Provider>
    );
}