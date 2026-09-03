import { useEffect, useState } from "react";
import { getBooks } from "../api/bookApi";
import BookCard from "../components/book/BookCard";

function BooksPage() {

    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadBooks = async () => {
            try {
                const data = await getBooks();
                setBooks(data);

            } catch (error) {
                console.error("Failed to load books", error);
                setError("Unable to load books. Please try again.");

            } finally {
                setLoading(false);
            }
        };

        void loadBooks();
    }, []);

    if (loading) {
        return <p>Loading books...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    if (books.length === 0) {
        return <p>No books are currently available.</p>;
    }

    return (
        <div>
            <h1>Online Bookstore</h1>

            <div>
                {books.map((book) => (
                    <BookCard
                        key={book.id}
                        book={book}
                    />
                ))}
            </div>
        </div>
    );
}

export default BooksPage;