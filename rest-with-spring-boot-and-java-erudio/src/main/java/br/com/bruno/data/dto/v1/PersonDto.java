package br.com.bruno.data.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@JsonPropertyOrder({"id", "firstName", "lastName", "address", "gender"})
@Data
public class PersonDto extends RepresentationModel<PersonDto> {

    @JsonProperty("id")
    @Mapping("id")
    private Long key;

    private String firstName;

    private String lastName;

    private String address;

    private String gender;
}
