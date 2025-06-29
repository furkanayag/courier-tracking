package com.courier_tracking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courier_log", indexes = {
        @Index(name = "index_courier", columnList = "courier")})
@Builder
public class CourierLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;

    @Column(name = "courierId")
    private Long courierId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "distance")
    private double distance;

    @ManyToOne
    private Store store;
}
