package br.com.bruno.data.dto.v1;

import lombok.Data;

@Data
public class PersonDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private String gender;
}
