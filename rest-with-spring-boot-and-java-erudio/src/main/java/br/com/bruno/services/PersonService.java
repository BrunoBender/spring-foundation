package br.com.bruno.services;

import br.com.bruno.commom.SecurityUtils;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Objects;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PagedResourcesAssembler<PersonDto> assembler;

    public PagedModel<EntityModel<PersonDto>> findAll(Pageable pageable, JwtAuthenticationToken token) {

        if(Objects.nonNull(token)) {
            var user = securityUtils.currentUser(token);

            logger.info("Usuário atual: " + user.getUsername());
        }

        logger.info("Finding all people!");

        var personPage = repository.findAll(pageable);
        var personDtoPage = personPage
                .map(this::toPersonDto)
                .map(this::addHateoasToFindById);

        Link link = getLinkHatoasToFindAll(token, pageable);

        return assembler.toModel(personDtoPage, link);
    }

    private Link getLinkHatoasToFindAll(
            JwtAuthenticationToken token,
            Pageable pageable
    ) {
        return linkTo(methodOn(PersonController.class).findAll(
                token,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "asc"
        )).withSelfRel();
    }

    private PersonDto toPersonDto(Person entity) {
        return DozerMapper.parseObject(entity, PersonDto.class);
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
