package br.com.bruno.mapper.custom;

import br.com.bruno.data.dto.v2.PersonDtoV2;
import br.com.bruno.entities.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {
    public PersonDtoV2 convertEntityToVoV2(Person entity) {
        PersonDtoV2 dtoV2 = new PersonDtoV2();
        dtoV2.setId(entity.getId());
        dtoV2.setFirstName(entity.getFirstName());
        dtoV2.setLastName(entity.getLastName());
        dtoV2.setAddress(entity.getAddress());
        dtoV2.setGender(entity.getGender());
        // Campo personalizado
        dtoV2.setBirthday(new Date());

        return dtoV2;
    }
}
