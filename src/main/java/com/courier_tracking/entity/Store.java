package com.courier_tracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store")
public class Store {
    @Id
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;
}
