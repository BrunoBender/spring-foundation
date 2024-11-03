package br.com.bruno.services;

import br.com.bruno.controllers.BookController;
import br.com.bruno.data.dto.v1.BookDto;
import br.com.bruno.entities.Book;
import br.com.bruno.exceptions.RequiredObjectISNullException;
import br.com.bruno.exceptions.ResourceNotFoundException;
import br.com.bruno.mapper.DozerMapper;
import br.com.bruno.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final Logger logger = Logger.getLogger(BookService.class.getName());

    @Autowired
    private BookRepository repository;

    public List<BookDto> findAll() {
        logger.info("Finding all books!");
        var dtoList = DozerMapper.parseListObjects(repository.findAll(), BookDto.class);

        dtoList.forEach(this::addHateoasToFindById);

        return dtoList;
    }

    public BookDto findById(Long id) throws ResourceNotFoundException {
        logger.info("Finding one book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for ID " + id));

        return addHateoasToFindById(DozerMapper.parseObject(entity, BookDto.class));
    }

    public BookDto create(BookDto bookDto) {

        if(Objects.isNull(bookDto)) throw new RequiredObjectISNullException();

        logger.info("Creating one book!");

        var savedEntity = repository.save(DozerMapper.parseObject(bookDto, Book.class));

        // Habilidando HATEOAS para a chamada, passando como link a provável próxima consulta de API.
        return addHateoasToFindById(DozerMapper.parseObject(savedEntity, BookDto.class));
    }

    public BookDto update(BookDto bookDto) {

        if(Objects.isNull(bookDto)) throw new RequiredObjectISNullException();

        logger.info("Updating one book!");

        var entity = repository.findById(bookDto.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for ID " + bookDto.getKey()));

        entity.setAuthor(bookDto.getAuthor());
        entity.setPrice(bookDto.getPrice());
        entity.setTitle(bookDto.getTitle());
        entity.setLaunchDate(bookDto.getLaunchDate());

        return addHateoasToFindById(DozerMapper.parseObject(repository.save(entity), BookDto.class));
    }

    public void delete(Long id) {
        logger.info("Deleting one book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found for ID " + id));

        repository.delete(entity);
    }

    private BookDto addHateoasToFindById(BookDto dto) {
        dto.add(
                linkTo(
                        methodOn(BookController.class).findById(dto.getKey())
                ).withSelfRel()
        );

        return dto;
    }
}
