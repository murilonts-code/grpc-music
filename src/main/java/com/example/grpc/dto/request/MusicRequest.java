package com.example.grpc.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MusicRequest {
    private Long id;
    private String name;
    private String artist;
}
