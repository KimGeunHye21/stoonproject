package com.example.stoonproject.dto;

import com.example.stoonproject.entity.Book;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String section;
    private int volume;
    private String genre1;
    private String genre2;
    private int rating;

    public Book toEntity() {
        return new Book(id, title, author, section, volume, genre1, genre2, rating);
    }


}
