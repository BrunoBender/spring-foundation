package br.com.bruno.services;

import br.com.bruno.data.dto.v1.PersonDto;
import br.com.bruno.data.dto.v2.PersonDtoV2;
import br.com.bruno.entities.Person;
import br.com.bruno.exceptions.ResourceNotFoundException;
import br.com.bruno.mapper.DozerMapper;
import br.com.bruno.mapper.custom.PersonMapper;
import br.com.bruno.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return DozerMapper.parseListObjects(repository.findAll(), PersonDto.class);
    }

    public PersonDto findById(Long id) throws ResourceNotFoundException {
        logger.info("Finding one person!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for ID " + id));

        return DozerMapper.parseObject(entity, PersonDto.class);
    }

    public PersonDto create(PersonDto person) {
        logger.info("Creating one person!");

        var savedEntity = repository.save(DozerMapper.parseObject(person, Person.class));

        return DozerMapper.parseObject(savedEntity, PersonDto.class);
    }

    public PersonDto update(PersonDto person) {
        logger.info("Updating one person!");

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for ID " + person.getId()));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return DozerMapper.parseObject(repository.save(entity), PersonDto.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for ID " + id));

        repository.delete(entity);
    }

    // Version 2

    public PersonDtoV2 createV2(PersonDtoV2 person) {
        logger.info("Creating one person! V2");

        var savedEntity = repository.save(DozerMapper.parseObject(person, Person.class));

        return personMapper.convertEntityToVoV2(savedEntity);
    }
}
