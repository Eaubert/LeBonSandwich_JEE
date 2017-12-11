package org.lpro.boundary;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.lpro.entity.Categorie;

@Stateless
@Path("categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategorieResource {
    
    @Inject
    CategorieManager cm;
    
    @GET
    public Response getCategories() {
//        JsonObject json = Json.createObjectBuilder()
//                .add("type", "collection")
//                .add("categories", getCategorieList())
//                .build();
//        return Response.ok(json).build();
        GenericEntity<List<Categorie>> liste = new GenericEntity<List<Categorie>>(this.cm.findAll()) {
        };
        return Response.ok(liste).build();
    }
    
    @GET
    @Path("{id}")
    public Response getOneCategorie(@PathParam("id") long id, @Context UriInfo uriInfo) {
        return Optional.ofNullable(cm.findById(id))
                //.map(c -> Response.ok(categorie2Json(c)).build())
                .map(c -> Response.ok(c).build())
                //.orElseThrow(() -> new CategorieNotFound("Ressource non disponible "+ uriInfo.getPath()));
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @POST
    public Response newCategorie(@Valid Categorie c, @Context UriInfo uriInfo) {
        Categorie newOne = this.cm.save(c);
        long id = newOne.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/"+id).build();
        return Response.created(uri).build();
    }
            
    @DELETE
    @Path("{id}")
    public Response suppression(@PathParam("id") long id) {
        this.cm.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @PUT
    @Path("{id}")
    public Categorie update(@PathParam("id") long id, Categorie c) {
        c.setId(id);
        return this.cm.save(c);
    }

    private JsonObject categorie2Json(Categorie c) {
        JsonObject json = Json.createObjectBuilder()
                .add("type", "resource")
                .add("categorie", buildJson(c))
                .build();
        return json;
    }
    
    private JsonArray getCategorieList() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        this.cm.findAll().forEach((c) -> {
            jab.add(buildJson(c));
            });
        return jab.build();
    }
    
    private JsonObject buildJson(Categorie c) {
        return Json.createObjectBuilder()
                .add("id",c.getId())
                .add("nom", c.getNom())
                .add("desc", c.getDescription())
                .build();
    }
}
