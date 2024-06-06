package com.example.grpc.resource;

import com.example.grpc.dto.request.MusicRequest;
import com.example.grpc.dto.response.MusicResponse;
import com.example.grpc.mapper.MusicMapper;
import com.example.grpc.proto.service.MusicList;
import com.example.grpc.proto.service.MusicService;
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

@Path("/musics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MusicResource {

    @GrpcClient("musicService")
    MusicService musicService;
    @Inject
    MusicMapper mapper;


    @POST
    public Uni<Response> create(MusicRequest musicRequest) {
        return musicService.create(mapper.musicRequestTomusic(musicRequest))
                .onItem()
                .transform(inserted -> Response.created(URI.create("/users/" + inserted.getId())).build());
    }

    @PUT
    public Uni<MusicResponse> update(MusicRequest musicRequest) {
        return musicService.update(mapper.musicRequestTomusic(musicRequest))
                .onItem().transform(mapper::musicToMusicResponse);
    }

    @GET
    public Uni<List<MusicResponse>> list() {
        return musicService.list(Empty.newBuilder().build())
                .onItem()
                .transform(MusicList::getResultListList)
                .map(mapper::musicListToMusicResponseList);
    }

    @GET
    @Path("/playlist/{id}")
    public Uni<List<MusicResponse>> musicsFromPlaylist(@PathParam("id") long id) {
        return musicService.musicFromPlaylist(Int64Value.of(id))
                .onItem()
                .transform(MusicList::getResultListList)
                .map(mapper::musicListToMusicResponseList);
    }

    @GET
    @Path("/{id}")
    public Uni<MusicResponse> findById(Long id) {
        return musicService.findById(Int64Value.of(id))
                .map(mapper::musicToMusicResponse);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(Long id) {
        return musicService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }

}
