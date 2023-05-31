package com.example.model.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photo")
public class PhotoEntity extends AbstractBaseEntity{

    @Column(name = "file_name_in_bucket")
    private String fileNameInBucket;

    @Column(name = "file_name")
    private String fileName;

    private BigDecimal size;

    private String type;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ToString.Exclude
    private AccountEntity account;
}
