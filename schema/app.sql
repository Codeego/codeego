DROP TABLE IF EXISTS pings;
CREATE TABLE pings (
  "id"         SERIAL PRIMARY KEY,
  "lang_id"    INTEGER,
  "editor_id"  INTEGER,
  "os_id"      INTEGER,
  "project_id" INTEGER,
  "branch_id"  INTEGER,
  "user_id"    INTEGER,
  "when"       TIMESTAMP
);

CREATE INDEX ON pings (lang_id);
CREATE INDEX ON pings (editor_id);
CREATE INDEX ON pings (os_id);
CREATE INDEX ON pings (project_id);
CREATE INDEX ON pings (branch_id);
CREATE INDEX ON pings (user_id);
CREATE INDEX ON pings ("when");
CREATE INDEX ON pings (ROUND(EXTRACT('epoch' FROM "when") / 600));