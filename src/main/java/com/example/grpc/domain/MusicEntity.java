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
@Table(name = "MUSIC")
public class MusicEntity {

    @Id
    @SequenceGenerator(name = "music_id_seq", allocationSize = 1, initialValue = 6)
    @GeneratedValue(generator = "music_id_seq")
    private Long id;
    private String name;
    private String artist;

    @ManyToMany(mappedBy = "musics")
    private Set<PlaylistEntity> playlists = new HashSet<>();
}
