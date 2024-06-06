package com.example.grpc.mapper;

import com.example.grpc.domain.PlaylistEntity;
import com.example.grpc.dto.request.PlaylistMusicRequest;
import com.example.grpc.dto.request.PlaylistRequest;
import com.example.grpc.dto.request.PlaylistResponse;
import com.example.grpc.proto.service.Playlist;
import com.example.grpc.proto.service.PlaylistMusic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface PlaylistMapper {

    PlaylistEntity PlaylistToEntity(Playlist playlist);

    Playlist entityToPlaylist(PlaylistEntity entity);

    List<Playlist> entityListToPlaylistList(List<PlaylistEntity> entityList);

    Playlist playlistRequestToPlaylist(PlaylistRequest playlistRequest);

    PlaylistMusic playlistMusicRequestToPlaylistMusic(PlaylistMusicRequest playlistMusicRequest);

    PlaylistResponse playlistToPlaylistResponse(Playlist playlist);

    List<PlaylistResponse> playlistListToPlaylistResponseList(List<Playlist> playlistListList);
}
