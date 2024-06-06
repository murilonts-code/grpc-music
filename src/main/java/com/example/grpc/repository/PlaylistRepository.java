package com.example.grpc.repository;

import com.example.grpc.domain.PlaylistEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.common.annotation.Blocking;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Blocking
public class PlaylistRepository implements PanacheRepository<PlaylistEntity> {
}
