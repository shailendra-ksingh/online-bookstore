import { useState } from "react";
import { useCart } from "../context/useCart";

function CheckoutPage({ onBackToCart, onOrderPlaced }) {

    const { cartItems, getCartTotal } = useCart();

    const [loading, setLoading] = useState(false);

    const handlePlaceOrder = async () => {

        setLoading(true);

        try {
            const order = {
                id: Date.now()
            };

            onOrderPlaced(order);

        } catch (error) {

            console.error("Failed to place order", error);

        } finally {

            setLoading(false);
        }
    };


    return (
        <div className="checkout-page">

            <div className="checkout-container">

                <h1>Order Summary</h1>

                {cartItems.map((item) => (

                    <div
                        className="checkout-item"
                        key={item.book.id}
                    >

                        <h3>
                            {item.book.title}
                        </h3>

                        <p>
                            Price: {Number(item.book.price).toFixed(2)}
                        </p>

                        <p>
                            Quantity: {item.quantity}
                        </p>

                        <p>
                            Item Total: 
                            {(
                                Number(item.book.price) *
                                item.quantity
                            ).toFixed(2)}
                        </p>

                    </div>

                ))}


                <div className="checkout-summary">

                    <h2>
                        Total: {Number(getCartTotal()).toFixed(2)}
                    </h2>


                    <button
                        type="button"
                        onClick={onBackToCart}
                        disabled={loading}
                    >
                        Back to Cart
                    </button>


                    <button
                        type="button"
                        onClick={handlePlaceOrder}
                        disabled={loading}
                    >
                        {loading
                            ? "Placing Order..."
                            : "Place Order"}
                    </button>

                </div>

            </div>

        </div>
    );
}

export default CheckoutPage;