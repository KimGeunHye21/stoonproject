package com.example.stoonproject.repository;

import com.example.stoonproject.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    ArrayList<Book> findAll(); // 모든 도서 조회

    @Query(value = "SELECT * FROM book WHERE id = :bookId", nativeQuery = true)
    Optional<Book> findById(Long bookId);

    @Query(value = "SELECT * FROM book WHERE REPLACE(title, ' ', '') LIKE REPLACE(CONCAT('%', :query, '%'), ' ', '')", nativeQuery = true)
    ArrayList<Book> findByQuery_title(@Param("query") String query);
    @Query(value = "SELECT * FROM book WHERE REPLACE(author1, ' ', '') LIKE REPLACE('%', :query, '%', ' ', '')" +
            "OR REPLACE(author2, ' ', '') LIKE REPLACE('%', :query, '%', ' ', '')", nativeQuery = true)
    ArrayList<Book> findByQuery_author(@Param("query") String query);
}