import { useCart } from "../../context/useCart";

function BookCard({ book }) {

    const { addToCart, loading } = useCart();

    const handleAddToCart = async () => {

        if (loading) return;

        await addToCart(book);
    };

    return (
        <div className="book-card">

            <h3>{book.title}</h3>

            <p className="book-author">
                Author: {book.author}
            </p>

            <p className="book-price">
                {Number(book.price ?? 0).toFixed(2)}
            </p>

            <button
                type="button"
                onClick={handleAddToCart}
                disabled={loading}
            >
                {loading ? "Adding..." : "Add to Cart"}
            </button>

        </div>
    );
}

export default BookCard;