package com.example.stoonproject.dto;

import com.example.stoonproject.entity.SearchHistory;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SearchHistoryDto {
    private Long id;
    private String query;
    private LocalDate date;

    public SearchHistory toEntity() {
        return new SearchHistory(id, query, date);
    }
}
