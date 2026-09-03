import { useCart } from "../context/useCart";


function CartPage({ onContinueShopping, onCheckout }) {

    const {
        cartItems,
        cartTotal,
        loading,
        error,
        increaseQuantity,
        decreaseQuantity,
        removeFromCart
    } = useCart();


    if (loading && cartItems.length === 0) {

        return (
            <div className="cart-page">

                <div className="cart-container">

                    <h1>Shopping Cart</h1>

                    <p>Loading cart...</p>

                </div>

            </div>
        );
    }


    return (
        <div className="cart-page">

            <div className="cart-container">

                <h1>Shopping Cart</h1>


                {error && (
                    <p
                        className="error-message"
                        role="alert"
                    >
                        {error}
                    </p>
                )}


                {loading && cartItems.length > 0 && (
                    <p>Updating cart...</p>
                )}


                {cartItems.length === 0 ? (

                    <div className="empty-cart">

                        <p>Your cart is empty.</p>

                        <button
                            type="button"
                            onClick={onContinueShopping}
                            disabled={loading}
                        >
                            Continue Shopping
                        </button>

                    </div>

                ) : (

                    <>

                        {cartItems.map((item) => (

                            <div
                                className="cart-item"
                                key={item.bookId}
                            >

                                <h3>{item.title}</h3>


                                <p>
                                    Price: 
                                    {Number(item.price).toFixed(2)}
                                </p>


                                <div className="quantity-controls">

                                    <button
                                        type="button"
                                        aria-label={`Decrease quantity of ${item.title}`}
                                        onClick={() =>
                                            decreaseQuantity(item.bookId)
                                        }
                                        disabled={
                                            loading ||
                                            item.quantity <= 1
                                        }
                                    >
                                        -
                                    </button>


                                    <span>
                                        {item.quantity}
                                    </span>


                                    <button
                                        type="button"
                                        aria-label={`Increase quantity of ${item.title}`}
                                        onClick={() =>
                                            increaseQuantity(item.bookId)
                                        }
                                        disabled={loading}
                                    >
                                        +
                                    </button>

                                </div>


                                <p>
                                    Item Total: 
                                    {Number(item.itemTotal).toFixed(2)}
                                </p>


                                <button
                                    type="button"
                                    className="remove-button"
                                    onClick={() =>
                                        removeFromCart(item.bookId)
                                    }
                                    disabled={loading}
                                >
                                    Remove
                                </button>

                            </div>

                        ))}


                        <div className="cart-summary">

                            <h2>
                                Total: 
                                {Number(cartTotal).toFixed(2)}
                            </h2>


                            <div className="cart-actions">

                                <button
                                    type="button"
                                    onClick={onContinueShopping}
                                    disabled={loading}
                                >
                                    Continue Shopping
                                </button>


                                <button
                                    type="button"
                                    onClick={onCheckout}
                                    disabled={loading}
                                >
                                    Checkout
                                </button>

                            </div>

                        </div>

                    </>

                )}

            </div>

        </div>
    );
}


export default CartPage;