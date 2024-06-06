package com.example.grpc.service;

import com.example.grpc.mapper.MusicMapper;
import com.example.grpc.proto.service.Music;
import com.example.grpc.proto.service.MusicList;
import com.example.grpc.proto.service.MusicService;
import com.example.grpc.repository.MusicRepository;
import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GrpcService
public class MusicServiceImpl implements MusicService {

    private final MusicRepository repository;
    private final MusicMapper mapper;

    public MusicServiceImpl(MusicRepository repository, MusicMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Uni<Music> create(Music request) {
        log.info("Creating a new user.");
        var entity = mapper.musicToEntity(request);
        entity.setId(null);
        return repository.persistAndFlush(entity).map(mapper::entityToMusic);
    }

    @Override
    public Uni<Music> update(Music request) {
        log.info("Updating the user. id: " + request.getId());
        var entity = mapper.musicToEntity(request);
        return repository.findById(request.getId())
                .onItem().ifNull().fail()
                .onItem().ifNotNull()
                .transformToUni(saved -> {
                    saved.setName(request.getName());
                    saved.setArtist(request.getArtist());
                    return repository.persistAndFlush(saved).onItem().transform(mapper::entityToMusic);
                });
    }

    @Override
    public Uni<Music> findById(Int64Value request) {
        log.info("Finding the user. id: " + request.getValue());
        var entity = repository.findById(request.getValue());
        return entity.onItem().ifNull().fail().map(mapper::entityToMusic);
    }

    @Override
    public Uni<MusicList> list(Empty request) {
        log.info("Listing all musics.");
        return repository.listAll().onItem()
                .transform(list -> MusicList.newBuilder()
                        .addAllResultList(mapper.entityListToMusicList(list))
                        .setResultCount(Int64Value.of(list.size()))
                        .build());
    }

    @Override
    public Uni<MusicList> musicFromPlaylist(Int64Value request) {
        return repository.findAllFromPlaylist(request)
                .onItem()
                .transform(list -> MusicList.newBuilder()
                        .addAllResultList(mapper.entityListToMusicList(list))
                        .setResultCount(Int64Value.of(list.size()))
                        .build());
    }

    @Override
    @ReactiveTransactional
    public Uni<BoolValue> delete(Int64Value request) {
        log.info("Deleting music by id. id: " + request.getValue());
        return repository.deleteById(request.getValue()).map(item -> BoolValue.newBuilder().setValue(item).build());
    }
}
