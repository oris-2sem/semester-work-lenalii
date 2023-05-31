package com.example.model.entity;


import com.example.model.constant.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "company")
public class CompanyEntity extends AbstractBaseEntity {

    private String name;

    private String description;

    @Column(name = "website_link")
    private String websiteLink;

    private String email;

    private String type;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private OffsetDateTime updateDate;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<HrEntity> hrs;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<VacancyEntity> vacancies;
}
