package org.example.yoluskutr.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "agent")
    private String agent;
}
