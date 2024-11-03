package br.com.bruno.unittests.mapper.mocks;

import br.com.bruno.data.dto.v1.BookDto;
import br.com.bruno.entities.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookDto mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDto> mockVOList() {
        List<BookDto> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setAuthor("Author test " + number);
        book.setLaunchDate(new Date());
        book.setTitle("Title test " + number);
        book.setId(number.longValue());
        book.setPrice(200.50);
        return book;
    }

    public BookDto mockVO(Integer number) {
        BookDto book = new BookDto();
        book.setAuthor("Author test " + number);
        book.setLaunchDate(new Date());
        book.setTitle("Title test " + number);
        book.setKey(number.longValue());
        book.setPrice(200.50);
        return book;
    }

}
