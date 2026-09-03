import { useCart } from "../../context/useCart";

function BookCard({ book }) {

    const { addToCart } = useCart();

    const handleAddToCart = async () => {
        await addToCart(book);
    };

    return (
        <div className="book-card">

            <h3>{book.title}</h3>

            <p className="book-author">
                Author: {book.author}
            </p>

            <p className="book-price">
                ₹{Number(book.price || 0).toFixed(2)}
            </p>

            <button
                type="button"
                onClick={handleAddToCart}
            >
                Add to Cart
            </button>

        </div>
    );
}

export default BookCard;