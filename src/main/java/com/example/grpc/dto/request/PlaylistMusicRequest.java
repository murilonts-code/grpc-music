package com.example.grpc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlaylistMusicRequest {

    @JsonProperty("music_id")
    private long musicId;

    @JsonProperty("playlist_id")
    private long playlistId;

}
