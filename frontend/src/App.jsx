import { useState } from "react";
import "./App.css";

import { AuthProvider } from "./context/AuthProvider.jsx";
import { useAuth } from "./context/useAuth";

import { CartProvider } from "./context/CartProvider.jsx";
import { useCart } from "./context/useCart";

import BooksPage from "./pages/BooksPage";
import CartPage from "./pages/CartPage";
import CheckoutPage from "./pages/CheckoutPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";


function BookstoreApplication() {

    const { logout } = useAuth();
    const { getCartItemCount } = useCart();

    const [showCart, setShowCart] = useState(false);
    const [showCheckout, setShowCheckout] = useState(false);


    const handleLogout = () => {

        setShowCart(false);
        setShowCheckout(false);

        logout();
    };


    // Checkout page
    if (showCheckout) {

        return (
            <div className="app">

                <div className="navbar">

                    <h2>Online Bookstore</h2>

                    <button
                        type="button"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>

                </div>


                <CheckoutPage

                    onBackToCart={() => {
                        setShowCheckout(false);
                        setShowCart(true);
                    }}

                    onOrderPlaced={(order) => {

                        alert(
                            `Order placed successfully!\nOrder ID: ${order.orderId}`
                        );

                        setShowCheckout(false);
                        setShowCart(false);
                    }}

                />

            </div>
        );
    }


    // Cart page
    if (showCart) {

        return (
            <div className="app">

                <div className="navbar">

                    <h2>Online Bookstore</h2>

                    <button
                        type="button"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>

                </div>


                <CartPage

                    onContinueShopping={() =>
                        setShowCart(false)
                    }

                    onCheckout={() => {
                        setShowCheckout(true);
                        setShowCart(false);
                    }}

                />

            </div>
        );
    }


    // Books page
    return (
        <div className="app">

            <div className="navbar">

                <h2>Online Bookstore</h2>


                <div className="nav-actions">

                    <button
                        type="button"
                        onClick={() => setShowCart(true)}
                    >
                        Cart ({getCartItemCount()})
                    </button>


                    <button
                        type="button"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>

                </div>

            </div>


            <main className="content">

                <BooksPage />

            </main>

        </div>
    );
}


function Application() {

    const { isAuthenticated } = useAuth();

    const [showRegister, setShowRegister] = useState(false);


    // User not logged in
    if (!isAuthenticated) {

        if (showRegister) {

            return (
                <RegisterPage
                    onRegistrationComplete={() =>
                        setShowRegister(false)
                    }
                />
            );
        }


        return (
            <LoginPage

                onLoginSuccess={() => {
                    setShowRegister(false);
                }}

                onRegisterClick={() => {
                    setShowRegister(true);
                }}

            />
        );
    }


    // CartProvider is mounted only after authentication.
    // This prevents cart API calls before login.
    // On logout, CartProvider unmounts and cart state is reset.
    return (
        <CartProvider>

            <BookstoreApplication />

        </CartProvider>
    );
}


function App() {

    return (
        <AuthProvider>

            <Application />

        </AuthProvider>
    );
}


export default App;