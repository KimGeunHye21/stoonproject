package com.example.stoonproject.service;

import com.example.stoonproject.repository.SearchHistoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class SearchTop {
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    // 저장된 인기 검색어 반환
    @Getter
    private ArrayList<String> popularSearches;

    //SearchTop 빈을 생성하고 의존성이 주입된 후에 init() 메서드가 자동으로 호출되어 함수가 실행
    @PostConstruct
    public void init() {
        calculateAndSavePopularSearches();
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void calculateAndSavePopularSearches() {
        // SearchHistoryRepository를 사용하여 DB에서 최근 4개월간의 검색어를 가져와서 계산한 후에 저장
        LocalDate startDate = LocalDate.now().minusMonths(4);
        popularSearches = calculatePopularSearches(searchHistoryRepository.findRecentSearchQueries(startDate));

    }

    // 실제로 인기 검색어를 계산하는 로직
    private ArrayList<String> calculatePopularSearches(ArrayList<String> searchHistory) {

        // 검색 로직 구현
        Map<String, Integer> searchCounts = new HashMap<>();

        for (String query: searchHistory) {
            //query에 해당하는 키가 있다면 query value + 1하여 저장, 없다면 1을 새로 저장
            searchCounts.put(query, searchCounts.getOrDefault(query, 0) + 1);
        }
        for (String key: searchCounts.keySet()) {
            System.out.println(key + ": " + searchCounts.get(key));
        }

        // 검색어의 발생 횟수를 기준으로 내림차순으로 정렬
        List<Map.Entry<String, Integer>> sortedSearchCounts = new ArrayList<>(searchCounts.entrySet());
        sortedSearchCounts.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        // 상위 10개의 검색어를 추출하여 반환
        ArrayList<String> topQueries = new ArrayList<>();
        int limit = Math.min(3, sortedSearchCounts.size());
        for (int i = 0; i < limit; i++) {
            topQueries.add(sortedSearchCounts.get(i).getKey());
        }

        System.out.println(topQueries.get(0));
        System.out.println(topQueries.get(1));
        System.out.println(topQueries.get(2));

        return topQueries;
    }




}
