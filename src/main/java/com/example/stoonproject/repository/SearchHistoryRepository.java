package com.example.stoonproject.repository;

import com.example.stoonproject.entity.SearchHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    @Override
    ArrayList<SearchHistory> findAll();

    //최근4개월 동안의 검색어를 반환하는 쿼리
    @Query(value = "SELECT query FROM Search_History WHERE date >= :startDate", nativeQuery = true)
    ArrayList<String> findRecentSearchQueries(@Param("startDate") LocalDate startDate);

    @Query(value = "SELECT query FROM Search_History", nativeQuery = true)
    ArrayList<String> findAllSearchQueries();



}
