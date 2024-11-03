package br.com.bruno.data.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@JsonPropertyOrder({"id", "author", "launchDate", "price", "title"})
@Data
public class BookDto extends RepresentationModel<BookDto> {

    @JsonProperty("id")
    @Mapping("id")
    private Long key;

    private String author;

    private Date launchDate;

    private Double price;

    private String title;
}
