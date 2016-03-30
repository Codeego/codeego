-- DROP TABLE IF EXISTS pings;
-- CREATE TABLE pings (
--   "id"         SERIAL PRIMARY KEY,
--   "lang_id"    INTEGER,
--   "editor_id"  INTEGER,
--   "os_id"      INTEGER,
--   "project_id" INTEGER,
--   "branch_id"  INTEGER,
--   "user_id"    INTEGER,
--   "when"       TIMESTAMP
-- );
--
-- CREATE INDEX ON pings (lang_id);
-- CREATE INDEX ON pings (editor_id);
-- CREATE INDEX ON pings (os_id);
-- CREATE INDEX ON pings (project_id);
-- CREATE INDEX ON pings (branch_id);
-- CREATE INDEX ON pings (user_id);
-- CREATE INDEX ON pings ("when");
-- CREATE INDEX ON pings (ROUND(EXTRACT('epoch' FROM "when") / 600));
--
-- DO
-- $do$
-- BEGIN
--   FOR i IN 1..1000000 LOOP
--     CASE floor(random() * 10)
--       WHEN 1 THEN
--         INSERT INTO pings
--           (lang_id, editor_id, os_id, project_id, branch_id, user_id, "when")
--           VALUES
--           (1,1,1,1,1,1, (NOW() + INTERVAL '19 month') + i * INTERVAL '1 second');
--       ELSE
--     END CASE;
--   END LOOP;
-- END
-- $do$;

-- SELECT
--   DISTINCT p1."when"
-- FROM pings p1
--   LEFT JOIN pings p2
--     ON (p2."when" >= p1."when" - (INTERVAL '600 seconds')
--         AND p2."when" < p1."when")
-- WHERE p2."when" IS NULL;

TRUNCATE pings;

-- INSERT INTO pings
-- (lang_id, editor_id, os_id, project_id, branch_id, user_id, "when")
-- VALUES
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:00'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:01'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:02'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:03'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:04'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:05'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:12'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:13'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:14'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:25'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:26'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:27'),
--   (0, 0, 0, 0, 0, 0, '2016-03-19 00:00:40');

SELECT (SELECT COUNT(1)
        FROM pings
        WHERE "when" >= chain_head AND ("when" < chain_tail OR chain_tail IS NULL)) AS counter
FROM (
       SELECT
         x AS chain_head,
         LEAD(x) OVER (ORDER BY x) AS chain_tail
       FROM (
              SELECT
                "when" AS x,
                "when" - LAG("when") OVER (ORDER BY "when") AS diff
              FROM pings
            ) AS i
       WHERE diff > '600 seconds' OR diff IS NULL) AS chains;