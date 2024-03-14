package com.ecommerce.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="country")
@Data
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    //TODO: Set up one-to-many with states
    @OneToMany(mappedBy = "country")
    @JsonIgnore
    private List<State> states;
}

