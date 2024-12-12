package br.com.bruno.unittests.services;

import br.com.bruno.data.dto.v1.BookDto;
import br.com.bruno.entities.Book;
import br.com.bruno.exceptions.RequiredObjectISNullException;
import br.com.bruno.repositories.BookRepository;
import br.com.bruno.services.BookService;
import br.com.bruno.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    private BookRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
    }

    @Test
    void findAll() {
        List<Book> bookList = input.mockEntityList();
        bookList.forEach(this::testFindById);
    }

    @Test
    void findById() {
        testFindById(input.mockEntity(1));
    }

    @Test
    void create() {
        Book entity = input.mockEntity();
        entity.setId(null);

        Book persistedEntity = entity;
        persistedEntity.setId(1L);

        BookDto entityDto = input.mockVO();
        entityDto.setKey(null);

        when(
                repository.save(any(Book.class))
        ).thenReturn(persistedEntity);

        testHateosBookDto(service.create(entityDto));
    }

    @Test
    void createWithNullBook() {
        Exception exception = assertThrows(RequiredObjectISNullException.class, () -> service.create(null));

        String exectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(exectedMessage));
    }

    @Test
    void updateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectISNullException.class, () -> service.update(null));

        String exectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(exectedMessage));
    }

    @Test
    void update() {
        Book entity = input.mockEntity(1);

        BookDto entityDto = input.mockVO(1);

        when(
                repository.findById(entityDto.getKey())
        ).thenReturn(Optional.of(entity));

        when(
                repository.save(entity)
        ).thenReturn(entity);

        testHateosBookDto(service.update(entityDto));
    }

    @Test
    void delete() {
        Book entity = input.mockEntity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }

    private void testFindById(Book book) {
        Long ID = book.getId();

        //Para não executar de fato a chamada ao repository, graças ao Mockito podemos mockar o retorno deste método!
        when(repository.findById(ID)).thenReturn(Optional.of(book));

        testHateosBookDto(service.findById(ID));
    }

    private void testHateosBookDto(BookDto bookDto) {
        assertNotNull(bookDto);
        assertNotNull(bookDto.getKey());
        assertNotNull(bookDto.getLinks());
        assertTrue(bookDto.getLinks().toString().contains("</api/book/" + bookDto.getKey() + ">;rel=\"self\""));
    }

}