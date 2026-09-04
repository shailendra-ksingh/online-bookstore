import { useState } from "react";
import "./App.css";

import { AuthProvider } from "./context/AuthProvider.jsx";
import { useAuth } from "./context/useAuth";

import { CartProvider } from "./context/CartProvider.jsx";
import { useCart } from "./context/useCart";

import Navbar from "./components/Navbar";
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

    const handleOrderPlaced = (order) => {
        alert(`Order placed successfully!\nOrder ID: ${order.orderId}`);

        setShowCheckout(false);
        setShowCart(false);
    };

    const handleBackToCart = () => {
        setShowCheckout(false);
        setShowCart(true);
    };

    const handleCheckout = () => {
        setShowCheckout(true);
        setShowCart(false);
    };

    if (showCheckout) {
        return (
            <div className="app">
                <Navbar onLogout={handleLogout} />

                <CheckoutPage
                    onBackToCart={handleBackToCart}
                    onOrderPlaced={handleOrderPlaced}
                />
            </div>
        );
    }

    if (showCart) {
        return (
            <div className="app">
                <Navbar onLogout={handleLogout} />

                <CartPage
                    onContinueShopping={() => setShowCart(false)}
                    onCheckout={handleCheckout}
                />
            </div>
        );
    }

    return (
        <div className="app">
            <Navbar
                onLogout={handleLogout}
                showCartButton={true}
                cartCount={getCartItemCount()}
                onCartClick={() => setShowCart(true)}
            />

            <main className="content">
                <BooksPage />
            </main>
        </div>
    );
}

function Application() {
    const { isAuthenticated } = useAuth();
    const [showRegister, setShowRegister] = useState(false);

    if (!isAuthenticated) {
        if (showRegister) {
            return (
                <RegisterPage
                    onRegistrationComplete={() => setShowRegister(false)}
                />
            );
        }

        return (
            <LoginPage
                onLoginSuccess={() => setShowRegister(false)}
                onRegisterClick={() => setShowRegister(true)}
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