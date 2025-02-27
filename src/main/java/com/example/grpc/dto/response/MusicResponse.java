package com.example.grpc.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MusicResponse {
    private Long id;
    private String name;
    private String artist;
}
