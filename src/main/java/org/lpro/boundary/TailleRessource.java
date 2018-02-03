package org.lpro.boundary;

import org.lpro.entity.Taille;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless
@Path("tailles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TailleRessource {

    @Inject
    TailleManager tm;

    @Inject
    SandwichManager sm;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getTailles() {
        JsonObject json = Json.createObjectBuilder()
                .add("type", "collection")
                .add("tailles", this.getTaillesList())
                .build();
        return Response.ok(json).build();
    }

    private JsonArray getTaillesList() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        this.tm.findAll().forEach((t) -> {
            jab.add(buildJson(t));
        });
        return jab.build();
    }

    public static JsonObject buildJson(Taille t) {

        JsonObject details = Json.createObjectBuilder()
                .add("id", t.getId())
                .add("nom", t.getNom())
                .build();

        return Json.createObjectBuilder()
                .add("taille", details)
                .build();
    }

    private JsonObject taille2Json(Taille t) {
        return Json.createObjectBuilder()
                .add("type", "resource")
                .add("taille", this.buildJson(t))
                .build();
    }
}
