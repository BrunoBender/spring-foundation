package br.com.bruno.data.dto.v2;

import lombok.Data;

import java.util.Date;

@Data
public class PersonDtoV2 {

    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private String gender;

    //new atribute

    private Date birthday;
}
