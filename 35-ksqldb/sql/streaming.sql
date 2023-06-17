CREATE TABLE titles (
    id INT PRIMARY KEY, 
    title VARCHAR
) WITH ( 
    KAFKA_TOPIC='titles', 
    VALUE_FORMAT='JSON', 
    PARTITIONS=4
);

CREATE TYPE season_length AS STRUCT<season_id INT, episode_count INT> ;

CREATE STREAM production_changes (
    rowkey VARCHAR KEY,
    uuid INT,
    title_id INT,
    change_type VARCHAR,
    before season_length,
    after season_length,
    created_at VARCHAR
) WITH (
    KAFKA_TOPIC='production_changes',
    PARTITIONS='4',
    VALUE_FORMAT='JSON',
    TIMESTAMP='created_at',
    TIMESTAMP_FORMAT='yyyy-MM-dd HH:mm:ss'
);

CREATE STREAM season_length_changes
WITH (
    KAFKA_TOPIC = 'season_length_changes',
    VALUE_FORMAT = 'JSON',
    PARTITIONS = 4,
    REPLICAS = 1
) AS SELECT
    ROWKEY,
    title_id,
    IFNULL(after->season_id, before->season_id) AS season_id,
    before->episode_count AS old_episode_count,
    after->episode_count AS new_episode_count,
    created_at
FROM production_changes
WHERE change_type = 'season_length'
EMIT CHANGES;

INSERT INTO titles VALUES (1, 'Stranger Things');
INSERT INTO titles VALUES (2, 'Black Mirror');
INSERT INTO titles VALUES (3, 'Bojack Horseman');

INSERT INTO production_changes (uuid, title_id, change_type, before, 
    after, created_at)
VALUES (1, 1, 'season_length',
    STRUCT(season_id := 1, episode_count := 12),
    STRUCT(season_id := 1, episode_count := 8),
    '2021-02-08 10:00:00'
);

INSERT INTO production_changes (ROWKEY, ROWTIME, uuid, title_id, 
    change_type, before, after, created_at)
VALUES ('2', 1581161400000, 2, 2, 'release_date',
    STRUCT(season_id := 1, release_date := '2021-05-27'),
    STRUCT(season_id := 1, release_date := '2021-08-18'),
    '2021-02-08 10:00:00'
);