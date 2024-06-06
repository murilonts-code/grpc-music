package com.example.grpc.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class PlaylistMusicResponse {
    private Long id;
    private String name;
    private Set<MusicResponse> musics = new HashSet<>();
}
