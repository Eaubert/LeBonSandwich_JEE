package org.lpro.boundary;

import org.lpro.entity.Categorie;
import org.lpro.entity.Sandwich;
import org.lpro.provider.Secured;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

@Stateless
@Path("categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategorieRessource {

    @Inject
    CategorieManager cm;

    @Inject
    SandwichManager sm;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getCategories() {
        JsonObject json = Json.createObjectBuilder()
                .add("type", "collection")
                .add("categories", this.getCategoriesList())
                .build();
        return Response.ok(json).build();
    }

    @GET
    @Path("{id}")
    public Response getOneCategorie(@PathParam("id") String id, @Context UriInfo uriInfo) throws Throwable {
        return Optional.ofNullable(this.cm.findById(id))
                .map(c -> Response.ok(categorie2Json(c)).build())
                .orElseThrow(() -> new CategorieNotFound("Ressource non disponible" + uriInfo.getPath()));
    }

    @GET
    @Path("{id}/sandwichs")
    public Response getCategorieSandwichs(@PathParam("id") String id, @Context UriInfo uriInfo) {
        Categorie c = this.cm.findById(id);
        Set<Sandwich> sandwichs = c.getSandwich();

        JsonArrayBuilder jab = Json.createArrayBuilder();
        sandwichs.forEach((s) -> {
            jab.add(SandwichRessource.buildJson(s));
        });

        JsonObject json = Json.createObjectBuilder()
                .add("type", "collection")
                .add("meta", Json.createObjectBuilder().add("count", sandwichs.size()).build())
                .add("sandwichs", jab.build())
                .build();

        return Response.ok(json).build();
    }

    @POST
    @Secured
    @Path("{id}/sandwichs")
    public Response addSandwichToCategorie(@PathParam("id") String catId, @Context UriInfo uriInfo, Sandwich sand) {
        Sandwich s = this.sm.addSandwich(catId, sand);
        URI uri = uriInfo.getAbsolutePathBuilder()
                .path("/")
                .path(s.getId())
                .build();
        return Response.created(uri).entity(SandwichRessource.buildJson(s)).build();
    }

    @POST
    @Secured
    public Response newCategorie(@Valid Categorie c, @Context UriInfo uriInfo) {
        Categorie cat = this.cm.save(c);
        String id = cat.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/" + id).build();
        return Response.created(uri).entity(this.buildJson(cat)).build();
    }

    @DELETE
    @Secured
    @Path("{id}")
    public Response removeCategorie(@PathParam("id") String id) {
        this.cm.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Secured
    @Path("{id}")
    public Categorie updateCategorie(@PathParam("id") String id, Categorie c) {
        c.setId(id);
        return this.cm.save(c);
    }

    private JsonArray getCategoriesList() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        this.cm.findAll().forEach((c) -> {
            jab.add(buildJson(c));
        });
        return jab.build();
    }

    public static JsonObject buildJson(Categorie c) {
        JsonObject self = Json.createObjectBuilder()
                .add("href", "/categories/" + c.getId())
                .build();

        JsonObject linksSandwichs = Json.createObjectBuilder()
                .add("href", "/categories/" + c.getId() + "/sandwichs")
                .build();

        JsonArrayBuilder sandwichs = Json.createArrayBuilder();
        c.getSandwich().forEach((s) -> {
            JsonObject sandwich = Json.createObjectBuilder()
                    .add("id", s.getId())
                    .add("nom", s.getNom())
                    .build();
            sandwichs.add(sandwich);
        });

        JsonObject links = Json.createObjectBuilder()
                .add("self", self)
                .add("sandwichs", linksSandwichs)
                .build();

        JsonObject details = Json.createObjectBuilder()
                .add("id", c.getId())
                .add("nom", c.getNom())
                .add("description", c.getDescription())
                .add("sandwichs", sandwichs)
                .build();

        return Json.createObjectBuilder()
                .add("categorie", details)
                .add("links", links)
                .build();
    }

    private JsonObject categorie2Json(Categorie c) {
        return Json.createObjectBuilder()
                .add("type", "resource")
                .add("categorie", this.buildJson(c))
                .build();
    }
}
