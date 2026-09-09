package com.bookstore.repository;

import com.bookstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndBookId(
            Long cartId,
            Long bookId
    );

    void deleteByCartId(Long cartId);

    @Query("""
        select c
        from CartItem c
        join fetch c.book
        where c.cart.id = :cartId
    """)
    List<CartItem> findAllWithBooks(Long cartId);
}