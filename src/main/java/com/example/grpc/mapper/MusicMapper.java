package com.example.grpc.mapper;

import com.example.grpc.domain.MusicEntity;
import com.example.grpc.dto.request.MusicRequest;
import com.example.grpc.dto.response.MusicResponse;
import com.example.grpc.proto.service.Music;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface MusicMapper {

    MusicEntity musicToEntity(Music music);

    Music entityToMusic(MusicEntity entity);

    List<Music> entityListToMusicList(List<MusicEntity> entityList);

    Music musicRequestTomusic(MusicRequest musicRequest);

    MusicResponse musicToMusicResponse(Music music);

    List<MusicResponse> musicListToMusicResponseList(List<Music> musicList);
}
