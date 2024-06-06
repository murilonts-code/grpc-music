package com.example.grpc.resource;

import com.example.grpc.dto.request.PlaylistMusicRequest;
import com.example.grpc.dto.request.PlaylistRequest;
import com.example.grpc.dto.request.PlaylistResponse;
import com.example.grpc.mapper.PlaylistMapper;
import com.example.grpc.proto.service.PlaylistList;
import com.example.grpc.proto.service.PlaylistService;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/playlists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlaylistResource {

    @GrpcClient("playlistService")
    PlaylistService playlistService;
    @Inject
    PlaylistMapper mapper;


    @POST
    public Uni<Response> create(PlaylistRequest PlaylistRequest) {
        return playlistService.create(mapper.playlistRequestToPlaylist(PlaylistRequest))
                .onItem()
                .transform(inserted -> Response.created(URI.create("/playlists/" + inserted.getId())).build());
    }

    @POST
    @Path("/music")
    public Uni<Response> addMusicToPlaylist(PlaylistMusicRequest playlistMusicRequest) {
        return playlistService.addMusicToPlaylist(mapper.playlistMusicRequestToPlaylistMusic(playlistMusicRequest))
                .onItem()
                .transform(inserted -> Response.created(URI.create("/playlists/" + inserted.getId())).build());
    }

    @PUT
    public Uni<PlaylistResponse> update(PlaylistRequest PlaylistRequest) {
        return playlistService.update(mapper.playlistRequestToPlaylist(PlaylistRequest))
                .map(mapper::playlistToPlaylistResponse);
    }

    @GET
    public Uni<List<PlaylistResponse>> list() {
        return playlistService.list(Empty.newBuilder().build())
                .onItem().transform(PlaylistList::getResultListList)
                .map(mapper::playlistListToPlaylistResponseList);
    }


    @GET
    @Path("/user/{id}")
    public Uni<List<PlaylistResponse>> list(@PathParam("id") long id) {
        return playlistService.findByUserId(Int64Value.of(id))
                .onItem().transform(PlaylistList::getResultListList)
                .map(mapper::playlistListToPlaylistResponseList);
    }

    @GET
    @Path("/{id}")
    public Uni<PlaylistResponse> findById(Long id) {
        return playlistService.findById(Int64Value.of(id)).map(mapper::playlistToPlaylistResponse);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(Long id) {
        return playlistService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }

    @DELETE
    @Path("/music")
    public Uni<Response> removeMusicOnPlaylist(PlaylistMusicRequest playlistMusicRequest) {
        return playlistService.addMusicToPlaylist(mapper.playlistMusicRequestToPlaylistMusic(playlistMusicRequest))
                .onItem()
                .transform(inserted -> Response.created(URI.create("/playlists/" + inserted.getId())).build());
    }

}
