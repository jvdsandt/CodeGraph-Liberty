package com.cloudctrl.codegraph.rest;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.List;

import com.cloudctrl.codegraph.dbmodel.tables.pojos.Artifact;
import com.cloudctrl.codegraph.dbmodel.tables.pojos.MvGroup;
import com.cloudctrl.codegraph.dbmodel.tables.records.MvGroupRecord;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import static com.cloudctrl.codegraph.dbmodel.Tables.ARTIFACT;
import static com.cloudctrl.codegraph.dbmodel.Tables.ARTIFACT_VERSION;
import static com.cloudctrl.codegraph.dbmodel.Tables.MV_GROUP;

@ApplicationScoped
public class ArtifactRepository {

	private DataSource dataSource;

	private DSLContext ctx;


	@Resource(lookup = "jdbc/myDb")
	void setDataSource(DataSource ds) {
		this.dataSource = ds;
		this.ctx = DSL.using(dataSource, SQLDialect.POSTGRES);
	}

	public List<Artifact> findAllWithGroup(String groupId) {
		var query = ctx.select(ARTIFACT.ID, ARTIFACT.ARTIFACT_ID)
				.from(ARTIFACT)
				.where(ARTIFACT.GROUP_ID.eq(groupId));

		return query.stream()
				.map(rec -> new Artifact(rec.value1(), rec.value2(), groupId))
				.toList();
	}

	public List<String> findAllVersions(String groupId, String artifactId) {
		var query = ctx.select(ARTIFACT_VERSION.VERSION)
				.from(ARTIFACT_VERSION)
				.join(ARTIFACT).on(ARTIFACT_VERSION.ARTIFACT_ID.eq(ARTIFACT.ID))
				.where(ARTIFACT.GROUP_ID.eq(groupId).and(ARTIFACT.ARTIFACT_ID.eq(artifactId)));

		return query.stream()
				.map(rec -> rec.value1())
				.toList();
	}

	public List<MvGroup> findLatestGroups(int limit, OffsetDateTime before) {
		Condition cond = DSL.trueCondition();
		if (before != null) {
			cond = cond.and(MV_GROUP.LMAX.lt(before));
		}
		var query = ctx.select(MV_GROUP)
				.from(MV_GROUP)
				.where(cond)
				.orderBy(MV_GROUP.LMAX.desc())
				.limit(limit);
		return query.fetchInto(MvGroup.class);
	}

}
