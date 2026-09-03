import { useCart } from "../../context/useCart";

function BookCard({ book }) {

    const { addToCart } = useCart();

    const handleAddToCart = () => {

        // Create a clean book object before adding to cart
        const bookToAdd = {
            id: book.id,
            title: book.title,
            author: book.author,
            price: Number(book.price)
        };

        console.log("Adding book to cart:", bookToAdd);

        addToCart(bookToAdd);
    };

    return (
        <div className="book-card">

            <h3>{book.title}</h3>

            <p className="book-author">
                Author: {book.author}
            </p>

            <p className="book-price">
                {Number(book.price || 0).toFixed(2)}
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