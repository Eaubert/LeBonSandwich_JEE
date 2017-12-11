/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lpro.boundary;

import static java.lang.Math.log;
import java.net.URI;
import static java.time.Clock.system;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.lpro.entity.Sandwich;

/**
 *
 * @author moi
 */


@Stateless
@Path("sandwich")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SandwichResource {
 
@Inject
    SandwichManager sm;
    
    @GET
    public Response getSandwichs(@QueryParam("t") String ptype, @QueryParam("img") int pimg, @QueryParam("page") int page, @DefaultValue("10") @QueryParam("size") int size) {

        GenericEntity<List<Sandwich>> liste = null;
        
        if(pimg == 1 || ptype != null){
            if(pimg == 1 && ptype != null){
                liste = new GenericEntity<List<Sandwich>>(this.sm.findAllByTypeImg(ptype)) {
                };
            }else{
                if(pimg == 1){
                   liste = new GenericEntity<List<Sandwich>>(this.sm.findAllByImg()) {
                   };
                }
                if(ptype != null){
                   liste = new GenericEntity<List<Sandwich>>(this.sm.findAllByType(ptype)) {
                   };
                }
            }
        }else if(page > 0){
            liste = new GenericEntity<List<Sandwich>>(this.sm.findAllByPage(page, size)){
            };
        }else if(page <= 0){
            liste = new GenericEntity<List<Sandwich>>(this.sm.findAllByPage(1, size)){
            };
        }else{
            liste = new GenericEntity<List<Sandwich>>(this.sm.findAll()) {
            };
        }
        
        return Response.ok(liste).build();
    }
    
    @GET
    @Path("{id}")
    public Response getOneSandwich(@PathParam("id") long id, @Context UriInfo uriInfo) {
        return Optional.ofNullable(sm.findById(id))
                //.map(c -> Response.ok(sandwich2Json(c)).build())
                .map(s -> Response.ok(s).build())
                //.orElseThrow(() -> new SandwichNotFound("Ressource non disponible "+ uriInfo.getPath()));
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @POST
    public Response newSandwich(@Valid Sandwich s, @Context UriInfo uriInfo) {
        Sandwich newOne = this.sm.save(s);
        long id = newOne.getId();
        URI uri = uriInfo.getAbsolutePathBuilder().path("/"+id).build();
        return Response.created(uri).build();
    }
            
    @DELETE
    @Path("{id}")
    public Response suppression(@PathParam("id") long id) {
        this.sm.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    @PUT
    @Path("{id}")
    public Sandwich update(@PathParam("id") long id, Sandwich s) {
        s.setId(id);
        return this.sm.save(s);
    }

    private JsonObject sandwich2Json(Sandwich s) {
        JsonObject json = Json.createObjectBuilder()
                .add("type", "resource")
                .add("sandwich", buildJson(s))
                .build();
        return json;
    }
    
    private JsonArray getSandwichList() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        this.sm.findAll().forEach((s) -> {
            jab.add(buildJson(s));
            });
        return jab.build();
    }
    
    private JsonObject buildJson(Sandwich s) {
        return Json.createObjectBuilder()
                .add("id",s.getId())
                .add("nom", s.getNom())
                .add("desc", s.getDescription())
                .add("type_pain", s.getType_pain())
                .add("img", s.getImg())
                .build();
    }
}

