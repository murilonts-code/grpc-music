package com.example.grpc.service;

import com.example.grpc.mapper.PlaylistMapper;
import com.example.grpc.proto.service.Playlist;
import com.example.grpc.proto.service.PlaylistList;
import com.example.grpc.proto.service.PlaylistMusic;
import com.example.grpc.proto.service.PlaylistService;
import com.example.grpc.repository.MusicRepository;
import com.example.grpc.repository.PlaylistRepository;
import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@GrpcService
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository repository;
    private final PlaylistMapper mapper;
    private final MusicRepository musicRepository;

    public PlaylistServiceImpl(PlaylistRepository repository, PlaylistMapper mapper, MusicRepository musicRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.musicRepository = musicRepository;
    }

    @Override
    public Uni<Playlist> create(Playlist request) {
        log.info("Creating a new Playlist.");
        var entity = mapper.PlaylistToEntity(request);
        entity.setId(null);
        return repository.persistAndFlush(entity).map(mapper::entityToPlaylist);
    }

    @Override
    public Uni<Playlist> addMusicToPlaylist(PlaylistMusic request) {
        log.info("Creating a new Playlist.");
        var playlistEntityUni = repository.findById(request.getPlaylistId());
        var musicEntityUni = musicRepository.findById(request.getPlaylistId());

        return playlistEntityUni.onItem()
                .ifNull().fail()
                .onItem()
                .transformToUni(playlistEntity -> musicEntityUni.onItem()
                        .ifNull().fail()
                        .onItem().transformToUni(musicEntity -> {
                            playlistEntity.getMusics().add(musicEntity);
                            return repository.persistAndFlush(playlistEntity).onItem()
                                    .transform(mapper::entityToPlaylist);
                        }));
    }

    @Override
    public Uni<Playlist> update(Playlist request) {
        log.info("Updating the Playlist. id: " + request.getId());
        var entity = mapper.PlaylistToEntity(request);
        return repository.findById(request.getId())
                .onItem().ifNull().fail()
                .onItem().ifNotNull().transformToUni(saved ->
                {
                    saved.setName(request.getName());
                    saved.setName(request.getName());
                    return repository.persistAndFlush(saved).onItem().transform(mapper::entityToPlaylist);
                });
    }

    @Override
    public Uni<Playlist> findById(Int64Value request) {
        log.info("Finding the Playlist. id: " + request.getValue());
        var entity = repository.findById(request.getValue());
        return entity.onItem().ifNull().fail().map(mapper::entityToPlaylist);
    }

    @Override
    public Uni<PlaylistList> list(Empty request) {
        log.info("Listing all Playlists.");
        var entityList = repository.listAll();
        return entityList.onItem()
                .transform(list -> PlaylistList.newBuilder()
                        .addAllResultList(mapper.entityListToPlaylistList(list))
                        .setResultCount(Int64Value.of(list.size()))
                        .build());
    }

    @Override
    public Uni<PlaylistList> findByUserId(Int64Value request) {
        log.info("Listing all Playlists From User.");
        var entityList = repository.list("user.id", request.getValue());
        return entityList.onItem()
                .transform(list -> PlaylistList.newBuilder()
                        .addAllResultList(mapper.entityListToPlaylistList(list))
                        .setResultCount(Int64Value.of(list.size()))
                        .build());
    }

    @Override
    @ReactiveTransactional
    public Uni<BoolValue> delete(Int64Value request) {
        log.info("Deleting Playlist by id. id: " + request.getValue());
        return repository.deleteById(request.getValue()).map(item -> BoolValue.newBuilder().setValue(item).build());
    }
}
