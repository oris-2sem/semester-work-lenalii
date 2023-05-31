package com.example.model.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vacancy")
public class VacancyEntity extends AbstractBaseEntity{

    public enum Status {
        ACTIVE, NOT_ACTIVE
    }

    @Column(name = "name")
    private String name;

    private String description;

    private String city;

    @ManyToOne
    @JoinColumn(name = "hr_id", referencedColumnName = "id")
    private HrEntity hr;

    private Boolean remote;

    @Column(name = "address")
    private String address;

    private Long salary;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private OffsetDateTime updateDate;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdDate;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private CompanyEntity company;

    @OneToMany(mappedBy = "vacancy", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<TagEntity> tags;

    @ManyToMany(mappedBy = "vacancies")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<DeveloperEntity> developers;
}
