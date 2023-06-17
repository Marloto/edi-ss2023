CREATE STREAM season_length_changes_enriched
WITH (
    KAFKA_TOPIC = 'season_length_changes_enriched',
    VALUE_FORMAT = 'JSON',
    PARTITIONS = 4,
    TIMESTAMP='created_at',
    TIMESTAMP_FORMAT='yyyy-MM-dd HH:mm:ss'
) AS SELECT
    s.title_id,
    t.title,
    s.season_id,
    s.old_episode_count,
    s.new_episode_count,
    s.created_at
FROM season_length_changes s
INNER JOIN titles t
ON s.title_id = t.id
EMIT CHANGES;