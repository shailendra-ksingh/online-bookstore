function Navbar({
                    onLogout,
                    showCartButton = false,
                    cartCount = 0,
                    onCartClick
                }) {
    return (
        <div className="navbar">

            <h2>Online Bookstore</h2>

            <div className="nav-actions">

                {showCartButton && (
                    <button
                        type="button"
                        onClick={onCartClick}
                    >
                        Cart ({cartCount})
                    </button>
                )}

                <button
                    type="button"
                    onClick={onLogout}
                >
                    Logout
                </button>

            </div>

        </div>
    );
}

export default Navbar;
