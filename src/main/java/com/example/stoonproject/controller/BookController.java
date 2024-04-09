package com.example.stoonproject.controller;

import com.example.stoonproject.dto.BookDto;
import com.example.stoonproject.entity.SearchHistory;
import com.example.stoonproject.repository.SearchHistoryRepository;
import com.example.stoonproject.service.SearchTop;
import org.springframework.ui.Model;
import com.example.stoonproject.entity.Book;
import com.example.stoonproject.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @GetMapping("/book/new")
    public String newBookForm() {
        return "/book/new";
    }

    @PostMapping("book/register")
    public String Register(BookDto dto) { // 매개변수로 DTO 받아오기
        log.info(dto.toString());

        // 1. DTO를 엔티티로 변환
        Book book = dto.toEntity();
        log.info(book.toString());

        // 2. 리파지터리로 엔티티를 DB에 저장
        Book saved = bookRepository.save(book);
        log.info(saved.toString());

        String encodedTitle = URLEncoder.encode(book.getTitle(), StandardCharsets.UTF_8);
        return "redirect:/book/managementList?searchType=title&query=" + encodedTitle;
    }


    @GetMapping("/book/list")
    public String list(Model model) {

        // 1. 모든 데이터 가져오기
        ArrayList<Book> bookEntityList = bookRepository.findAll();

        // 2. 모델에 데이터 등록하기
        model.addAttribute("bookList", bookEntityList);

        // 3. 뷰 페이지 설정하기
        return "book/list";
    }


    @GetMapping("/book/managementList")
    public String index(@RequestParam(required = false) String query, @RequestParam(required = false) String searchType, Model model) {

        if (searchType == null) searchType = "title"; // 기본값은 제목검색으로 설정

        // 1. 데이터 가져오기
        ArrayList<Book> searchedEntityList;
        if(searchType.equals("all")) searchedEntityList = bookRepository.findByQuery_all(query);
        else if(searchType.equals("title")) searchedEntityList = bookRepository.findByQuery_title(query);
        else searchedEntityList = bookRepository.findByQuery_author(query);

        // 2. 모델에 데이터 등록하기
        model.addAttribute("searchType", searchType);
        model.addAttribute("query", (query != null) ? query : "");
        model.addAttribute("searchResults", searchedEntityList);

        // 뷰 페이지 설정하기
        return "book/admin/list";
    }

    @GetMapping("/book/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {

        // 수정할 데이터 가져오기
        Book bookEntity = bookRepository.findById(id).orElse(null);

        // 모델에 데이터 등록하기
        model.addAttribute("book", bookEntity);

        // 뷰 페이지 설정하기
        return "book/admin/edit";
    }

    @PostMapping("/book/update")
    public String update(BookDto dto) { // 매개변수로 DTO 받아오기
        log.info(dto.toString());

        // 1. DTO를 엔티티로 변환하기
        Book bookEntity = dto.toEntity();
        log.info(bookEntity.toString());

        // 2. 엔티티를 DB에 저장하기
        // 2-1. DB에서 기존 데이터 가져오기
        Book target = bookRepository.findById(bookEntity.getId()).orElse(null);
        // 2-2. 기존 데이터 값을 갱신하기
        if (target != null) bookRepository.save(bookEntity);

        String encodedTitle = URLEncoder.encode(bookEntity.getTitle(), StandardCharsets.UTF_8);
        return "redirect:/book/managementList?searchType=title&query=" + encodedTitle;
    }


    @GetMapping("/book/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        // 1. 삭제할 대상 가져오기
        Book target = bookRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 2. 대상 엔티티 삭제하기
        if (target != null) {
            bookRepository.delete(target);
            rttr.addFlashAttribute("msg", "'" + target.getTitle()+"' 도서를 삭제했습니다.");
        }

        return "redirect:/book/managementList";
    }


    @GetMapping("/book/search")
    public String search(@RequestParam(required = false) String query, @RequestParam(required = false) String searchType, Model model) {

        // 1. 검색한 데이터 출력하기
        if (searchType == null) searchType = "title"; // 기본값은 제목검색으로 설정

        // 1-1. 데이터 가져오기
        ArrayList<Book> searchedEntityList;
        if(searchType.equals("all")) searchedEntityList = bookRepository.findByQuery_all(query);
        else if (searchType.equals("title")) searchedEntityList = bookRepository.findByQuery_title(query);
        else searchedEntityList = bookRepository.findByQuery_author(query);

        // 1-2. 모델에 데이터 등록하기
        model.addAttribute("searchType", searchType);
        model.addAttribute("query", (query != null) ? query : "");
        model.addAttribute("searchResults", searchedEntityList);


        // 2. 검색순위를 위한 검색쿼리 DB에 저장

        // 2-1. DTO를 엔티티로 변환
        if (!query.equals("")) {
            SearchHistory searchHistoryEntity = new SearchHistory();
            searchHistoryEntity.setQuery(query);
            searchHistoryEntity.setDate(LocalDate.now());

            log.info(searchHistoryEntity.toString());

            // 2-2. 리파지터리로 엔티티를 DB에 저장

            SearchHistory saved = searchHistoryRepository.save(searchHistoryEntity);
            log.info(saved.toString());

            ArrayList<String> test_searchHistory = searchHistoryRepository.findAllSearchQueries();
            for (String str : test_searchHistory) System.out.println(str);


        }
        SearchTop searchTop = new SearchTop();
        ArrayList<String> popularSearches = searchTop.getPopularSearches();
        //model.addAttribute("popularSearches", popularSearches);


        // 3. 뷰 페이지 설정하기
        return "/book/search";
    }

}
