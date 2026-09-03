import { useState } from "react";
import { CartContext } from "./CartContext";

export function CartProvider({ children }) {

    const [cartItems, setCartItems] = useState([]);

    const addToCart = (book) => {

        setCartItems((previousItems) => {

            const existingItem = previousItems.find(
                (item) => item.book.id === book.id
            );

            if (existingItem) {

                return previousItems.map((item) =>
                    item.book.id === book.id
                        ? {
                            ...item,
                            quantity: item.quantity + 1
                        }
                        : item
                );
            }

            return [
                ...previousItems,
                {
                    book: book,
                    quantity: 1
                }
            ];
        });
    };


    const increaseQuantity = (bookId) => {

        setCartItems((previousItems) =>
            previousItems.map((item) =>
                item.book.id === bookId
                    ? {
                        ...item,
                        quantity: item.quantity + 1
                    }
                    : item
            )
        );
    };


    const decreaseQuantity = (bookId) => {

        setCartItems((previousItems) =>
            previousItems.map((item) =>
                item.book.id === bookId
                    ? {
                        ...item,
                        quantity: Math.max(
                            1,
                            item.quantity - 1
                        )
                    }
                    : item
            )
        );
    };


    const removeFromCart = (bookId) => {

        setCartItems((previousItems) =>
            previousItems.filter(
                (item) => item.book.id !== bookId
            )
        );
    };


    const getCartItemCount = () => {

        return cartItems.reduce(
            (total, item) => total + item.quantity,
            0
        );
    };


    // IMPORTANT: This was missing or named differently
    const getCartTotal = () => {

        return cartItems.reduce(
            (total, item) =>
                total + (item.book.price * item.quantity),
            0
        );
    };


    const value = {

        cartItems,

        addToCart,

        increaseQuantity,

        decreaseQuantity,

        removeFromCart,

        getCartItemCount,

        getCartTotal
    };


    return (
        <CartContext.Provider value={value}>
            {children}
        </CartContext.Provider>
    );
}