package br.com.bruno.services;

import br.com.bruno.controllers.PersonController;
import br.com.bruno.data.dto.v1.PersonDto;
import br.com.bruno.data.dto.v2.PersonDtoV2;
import br.com.bruno.entities.Person;
import br.com.bruno.exceptions.RequiredObjectISNullException;
import br.com.bruno.exceptions.ResourceNotFoundException;
import br.com.bruno.mapper.DozerMapper;
import br.com.bruno.mapper.custom.PersonMapper;
import br.com.bruno.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper personMapper;

    public List<PersonDto> findAll() {
        logger.info("Finding all people!");
        var dtoList = DozerMapper.parseListObjects(repository.findAll(), PersonDto.class);

        dtoList.forEach(this::addHateoasToFindById);

        return dtoList;
    }

    public PersonDto findById(Long id) throws ResourceNotFoundException {
        logger.info("Finding one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for ID " + id));

        return addHateoasToFindById(DozerMapper.parseObject(entity, PersonDto.class));
    }

    public PersonDto create(PersonDto person) {

        if(Objects.isNull(person)) throw new RequiredObjectISNullException();

        logger.info("Creating one person!");

        var savedEntity = repository.save(DozerMapper.parseObject(person, Person.class));

        // Habilidando HATEOAS para a chamada, passando como link a provável próxima consulta de API.
        return addHateoasToFindById(DozerMapper.parseObject(savedEntity, PersonDto.class));
    }

    public PersonDto update(PersonDto person) {

        if(Objects.isNull(person)) throw new RequiredObjectISNullException();

        logger.info("Updating one person!");

        var entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for ID " + person.getKey()));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setEnabled(person.getEnabled());

        return addHateoasToFindById(DozerMapper.parseObject(repository.save(entity), PersonDto.class));
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for ID " + id));

        repository.delete(entity);
    }

    @Transactional
    public PersonDto disablePerson(Long id) throws ResourceNotFoundException {
        logger.info("Disabling one person!");

        repository.disablePerson(id);

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for ID " + id));

        return addHateoasToFindById(DozerMapper.parseObject(entity, PersonDto.class));
    }

    // Version 2

    public PersonDtoV2 createV2(PersonDtoV2 person) {
        logger.info("Creating one person! V2");

        var savedEntity = repository.save(DozerMapper.parseObject(person, Person.class));

        return personMapper.convertEntityToVoV2(savedEntity);
    }

    private PersonDto addHateoasToFindById(PersonDto dto) {
        dto.add(
                linkTo(
                        methodOn(PersonController.class).findById(dto.getKey())
                ).withSelfRel()
        );

        return dto;
    }
}
