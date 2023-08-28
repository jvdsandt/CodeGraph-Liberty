package com.cloudctrl.codegraph.rest;

import javax.sql.DataSource;
import java.util.List;

import com.cloudctrl.codegraph.dbmodel.tables.pojos.Artifact;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import static com.cloudctrl.codegraph.dbmodel.Tables.ARTIFACT;
import static com.cloudctrl.codegraph.dbmodel.Tables.ARTIFACT_VERSION;

@ApplicationScoped
public class ArtifactRepository {

	private DataSource dataSource;

	private DSLContext create;


	@Resource(lookup = "jdbc/myDb")
	void setDataSource(DataSource ds) {
		this.dataSource = ds;
		this.create = DSL.using(dataSource, SQLDialect.POSTGRES);
	}

	public List<Artifact> findAllWithGroup(String groupId) {
		var query = create.select(ARTIFACT.ID, ARTIFACT.ARTIFACT_ID)
				.from(ARTIFACT)
				.where(ARTIFACT.GROUP_ID.eq(groupId));

		return query.stream()
				.map(rec -> new Artifact(rec.value1(), rec.value2(), groupId))
				.toList();
	}

	public List<String> findAllVersions(String groupId, String artifactId) {
		var query = create.select(ARTIFACT_VERSION.VERSION)
				.from(ARTIFACT_VERSION)
				.join(ARTIFACT).on(ARTIFACT_VERSION.ARTIFACT_ID.eq(ARTIFACT.ID))
				.where(ARTIFACT.GROUP_ID.eq(groupId).and(ARTIFACT.ARTIFACT_ID.eq(artifactId)));

		return query.stream()
				.map(rec -> rec.value1())
				.toList();
	}

}
