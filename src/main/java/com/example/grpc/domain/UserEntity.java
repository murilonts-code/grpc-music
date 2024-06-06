package com.example.grpc.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "USERS")
public class UserEntity {
    @Id
    @SequenceGenerator(name = "users_id_seq", allocationSize = 1, initialValue = 5)
    @GeneratedValue(generator = "users_id_seq")
    private Long id;
    private String name;
    private Long age;

    @OneToMany(mappedBy = "user")
    private Set<PlaylistEntity> playlists = new HashSet<>();
}
