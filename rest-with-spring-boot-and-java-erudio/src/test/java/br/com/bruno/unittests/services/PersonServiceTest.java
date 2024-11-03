package br.com.bruno.unittests.services;

import br.com.bruno.data.dto.v1.PersonDto;
import br.com.bruno.entities.Person;
import br.com.bruno.exceptions.RequiredObjectISNullException;
import br.com.bruno.repositories.PersonRepository;
import br.com.bruno.services.PersonService;
import br.com.bruno.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Person> personList = input.mockEntityList();
        personList.forEach(this::testFindById);
    }

    @Test
    void findById() {
        testFindById(input.mockEntity(1));
    }

    @Test
    void create() {
        Person entity = input.mockEntity();
        entity.setId(null);

        Person persistedEntity = entity;
        persistedEntity.setId(1L);

        PersonDto entityDto = input.mockVO();
        entityDto.setKey(null);

        when(
                repository.save(any(Person.class))
        ).thenReturn(persistedEntity);

        testHateosPersonDto(service.create(entityDto));
    }

    @Test
    void createWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectISNullException.class, () -> service.create(null));

        String exéctedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(exéctedMessage));
    }

    @Test
    void updateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectISNullException.class, () -> service.update(null));

        String exéctedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(exéctedMessage));
    }

    @Test
    void update() {
        Person entity = input.mockEntity(1);

        PersonDto entityDto = input.mockVO(1);

        when(
                repository.findById(entityDto.getKey())
        ).thenReturn(Optional.of(entity));

        when(
                repository.save(entity)
        ).thenReturn(entity);

        testHateosPersonDto(service.update(entityDto));
    }

    @Test
    void delete() {
        Person entity = input.mockEntity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }

    private void testFindById(Person person) {
        Long ID = person.getId();

        //Para não executar de fato a chamada ao repository, graças ao Mockito podemos mockar o retorno deste método!
        when(repository.findById(ID)).thenReturn(Optional.of(person));

        testHateosPersonDto(service.findById(ID));
    }

    private void testHateosPersonDto(PersonDto personDto) {
        assertNotNull(personDto);
        assertNotNull(personDto.getKey());
        assertNotNull(personDto.getLinks());
        assertTrue(personDto.getLinks().toString().contains("</person/" + personDto.getKey() + ">;rel=\"self\""));
    }

}