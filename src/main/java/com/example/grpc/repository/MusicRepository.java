package com.example.grpc.repository;

import com.example.grpc.domain.MusicEntity;
import com.google.protobuf.Int64Value;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@Blocking
@ApplicationScoped
public class MusicRepository implements PanacheRepository<MusicEntity> {
    public Uni<List<MusicEntity>> findAllFromPlaylist(Int64Value playlistId) {
        return list("""
                select m from MusicEntity m
                LEFT JOIN m.playlists pl
                where pl.id = ?1
                """, playlistId.getValue());
    }
}

