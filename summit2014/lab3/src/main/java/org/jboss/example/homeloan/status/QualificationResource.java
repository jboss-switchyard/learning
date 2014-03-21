package org.jboss.example.homeloan.status;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.example.homeloan.data.Qualification;


@Path("/")
public interface QualificationResource {

	@GET
	@Path("{ssn}")
	public Qualification status(@PathParam("ssn") String ssn);
	
}
