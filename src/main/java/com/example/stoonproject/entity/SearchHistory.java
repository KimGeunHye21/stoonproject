package com.example.stoonproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String query;

    @Column
    private LocalDate date;

    public Timestamp localDateTimeToTimestamp(LocalDateTime ldt) {
        return Timestamp.valueOf(ldt);
    }


}
