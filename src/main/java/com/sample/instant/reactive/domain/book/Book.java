package com.sample.instant.reactive.domain.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Cacheable
@Getter
@Setter
@NoArgsConstructor
public class Book extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_book")
    @SequenceGenerator(name = "seq_book", sequenceName = "seq_book", allocationSize = 1)
    @JsonIgnore
    private Long id;

    @Schema(description = "Name of the book", example = "Kane and Abel")
    private String name;

    @Schema(description = "ISBN identifier for the book", example = "9781250199591")
    private String isbn;

    @Schema(description = "Publication year of edition", example = "2012")
    private Integer publicationYear;

}
