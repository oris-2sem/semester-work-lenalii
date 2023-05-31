package com.example.model.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag")
public class TagEntity extends AbstractBaseEntity{

    @Column(name = "tag")
    private String tag;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", referencedColumnName = "id")
    private VacancyEntity vacancy;
}
