import { useCart } from "../context/useCart";

function CartPage({ onContinueShopping, onCheckout }) {

    const {
        cartItems,
        increaseQuantity,
        decreaseQuantity,
        removeFromCart,
        getCartTotal
    } = useCart();

    return (
        <div className="cart-page">

            <div className="cart-container">

                <h1>Shopping Cart</h1>

                {cartItems.length === 0 ? (

                    <div className="empty-cart">
                        <p>Your cart is empty.</p>

                        <button
                            type="button"
                            onClick={onContinueShopping}
                        >
                            Continue Shopping
                        </button>
                    </div>

                ) : (

                    <>
                        {cartItems.map((item) => (

                            <div
                                className="cart-item"
                                key={item.book.id}
                            >
                                <h3>{item.book.title}</h3>

                                <p>
                                    Price: {Number(item.book.price).toFixed(2)}
                                </p>

                                <div className="quantity-controls">

                                    <button
                                        type="button"
                                        onClick={() =>
                                            decreaseQuantity(item.book.id)
                                        }
                                    >
                                        -
                                    </button>

                                    <span>{item.quantity}</span>

                                    <button
                                        type="button"
                                        onClick={() =>
                                            increaseQuantity(item.book.id)
                                        }
                                    >
                                        +
                                    </button>

                                </div>

                                <p>
                                    Item Total: 
                                    {(
                                        Number(item.book.price) *
                                        item.quantity
                                    ).toFixed(2)}
                                </p>

                                <button
                                    type="button"
                                    className="remove-button"
                                    onClick={() =>
                                        removeFromCart(item.book.id)
                                    }
                                >
                                    Remove
                                </button>

                            </div>
                        ))}

                        <div className="cart-summary">

                            <h2>
                                Total: {getCartTotal().toFixed(2)}
                            </h2>

                            <div className="cart-actions">

                                <button
                                    type="button"
                                    onClick={onContinueShopping}
                                >
                                    Continue Shopping
                                </button>

                                <button
                                    type="button"
                                    onClick={onCheckout}
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