package br.com.bruno.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.bruno.data.dto.v1.PersonDto;
import br.com.bruno.entities.Person;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }
    
    public PersonDto mockVO() {
        return mockVO(0);
    }
    
    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonDto> mockVOList() {
        List<PersonDto> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }
    
    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test" + number);
        person.setEnabled(true);
        return person;
    }

    public PersonDto mockVO(Integer number) {
        PersonDto person = new PersonDto();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setKey(number.longValue());
        person.setLastName("Last Name Test" + number);
        person.setEnabled(true);
        return person;
    }

}
