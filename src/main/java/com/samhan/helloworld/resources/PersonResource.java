package com.samhan.helloworld.resources;

import com.samhan.helloworld.api.Person;
import com.samhan.helloworld.db.PersonDAO;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/people/{name}")
public class PersonResource {

    private final PersonDAO dao;

    private final AtomicInteger id = new AtomicInteger();

    public PersonResource(PersonDAO dao) {
        this.dao = dao;
    }

    @PUT
    public void createPerson(@PathParam("name") String name, @QueryParam("password") String password) {
        dao.insert(id.getAndIncrement(), name, password);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person helloPerson(@PathParam("name") String name) {
        return dao.findByName(name);
    }

//    /**
//     * @param name the name of the person to return
//     * @return person by name
//     */
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Person byName(@PathParam("name") String name) {
//
////        return name.equals(user.getName())
////                ? dao.findByName(name)
////                : new Person(0, "Anonymous", "");
//        return new Person(0, "Anonymous", "");
//    }
}
