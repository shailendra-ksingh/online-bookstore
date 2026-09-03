import { useState } from "react";
import { useCart } from "../context/useCart";
import { createOrder } from "../api/orderApi";


function CheckoutPage({ onBackToCart, onOrderPlaced }) {

    const {
        cartItems,
        cartTotal,
        loading: cartLoading,
        loadCart
    } = useCart();

    const [isPlacingOrder, setIsPlacingOrder] = useState(false);
    const [error, setError] = useState("");


    const handlePlaceOrder = async () => {

        if (cartItems.length === 0) {

            setError("Your cart is empty.");
            return;
        }

        setIsPlacingOrder(true);
        setError("");

        try {

            const order = await createOrder();

            // Refresh frontend cart because backend clears
            // the cart after successful order creation.
            try {

                await loadCart();

            } catch (cartError) {

                // Order was successful, so cart refresh failure
                // should not be treated as order failure.
                console.error(
                    "Order placed successfully, but cart refresh failed",
                    cartError
                );
            }

            onOrderPlaced(order);

        } catch (error) {

            console.error("Failed to place order", error);

            setError(
                "Unable to place order. Please try again."
            );

        } finally {

            setIsPlacingOrder(false);
        }
    };


    if (cartLoading && cartItems.length === 0) {

        return (
            <div className="checkout-page">

                <div className="checkout-container">

                    <p>Loading order summary...</p>

                </div>

            </div>
        );
    }


    return (
        <div className="checkout-page">

            <div className="checkout-container">

                <h1>Order Summary</h1>


                {error && (
                    <p className="error-message">
                        {error}
                    </p>
                )}


                {cartItems.length === 0 ? (

                    <div className="empty-cart">

                        <p>Your cart is empty.</p>

                        <button
                            type="button"
                            onClick={onBackToCart}
                            disabled={isPlacingOrder}
                        >
                            Back to Cart
                        </button>

                    </div>

                ) : (

                    <>

                        {cartItems.map((item) => (

                            <div
                                className="checkout-item"
                                key={item.bookId}
                            >

                                <h3>
                                    {item.title}
                                </h3>


                                <p>
                                    Price: 
                                    {Number(item.price).toFixed(2)}
                                </p>


                                <p>
                                    Quantity: {item.quantity}
                                </p>


                                <p>
                                    Item Total: 
                                    {Number(item.itemTotal).toFixed(2)}
                                </p>

                            </div>

                        ))}


                        <div className="checkout-summary">

                            <h2>
                                Total:
                                {Number(cartTotal).toFixed(2)}
                            </h2>


                            <button
                                type="button"
                                onClick={onBackToCart}
                                disabled={isPlacingOrder}
                                style={{ marginRight: "12px" }}
                            >
                                Back to Cart
                            </button>


                            <button
                                type="button"
                                onClick={handlePlaceOrder}
                                disabled={
                                    isPlacingOrder ||
                                    cartLoading
                                }
                            >
                                {isPlacingOrder
                                    ? "Placing Order..."
                                    : "Place Order"}
                            </button>

                        </div>

                    </>

                )}

            </div>

        </div>
    );
}


export default CheckoutPage;