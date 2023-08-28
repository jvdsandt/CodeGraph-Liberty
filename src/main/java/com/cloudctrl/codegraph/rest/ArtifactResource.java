package com.cloudctrl.codegraph.rest;

import java.util.List;

import com.cloudctrl.codegraph.dbmodel.tables.pojos.Artifact;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class ArtifactResource {

	@Inject
	private ArtifactRepository artifactRepo;

	@GET
	public String hoi() {
		return "hoi";
	}

	@GET
	@Path("/artifacts/{groupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Artifact> getGroupArtifacts(
			@PathParam("groupId") String groupId) {
		return artifactRepo.findAllWithGroup(groupId);
	}

	@GET
	@Path("/artifacts/{groupId}/{artifactId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getGroupArtifactVersions(
			@PathParam("groupId") String groupId,
			@PathParam("artifactId") String artifactId) {
		return artifactRepo.findAllVersions(groupId, artifactId);
	}
}
