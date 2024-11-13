package com.cloudctrl.codegraph.rest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import com.cloudctrl.codegraph.dbmodel.tables.pojos.Artifact;
import com.cloudctrl.codegraph.dbmodel.tables.pojos.MvGroup;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.jooq.Record2;

@Path("/")
public class ArtifactResource {

	private ArtifactRepository artifactRepo;

	@Inject
	public ArtifactResource(ArtifactRepository repo) {
		super();
		this.artifactRepo = repo;
	}

	@GET
	public String hoi() {
		return "hoi";
	}

	@GET
	@Path("/artifacts")
	@Produces(MediaType.APPLICATION_JSON)
	public List<MvGroup> getGroupArtifacts(@QueryParam("before") OffsetDateTime before) {
		var list = artifactRepo.findLatestGroups(20, before);
		return list;
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
