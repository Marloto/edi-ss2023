CREATE STREAM start_watching_events (
    session_id STRING,
    title_id INT,
    created_at STRING
) WITH (
    KAFKA_TOPIC='start_watching_events',
    VALUE_FORMAT='JSON',
    PARTITIONS=4,
    TIMESTAMP='created_at',
    TIMESTAMP_FORMAT='yyyy-MM-dd HH:mm:ss'
);


CREATE STREAM stop_watching_events (
    session_id STRING,
    title_id INT,
    created_at STRING
) WITH (
    KAFKA_TOPIC='stop_watching_events',
    VALUE_FORMAT='JSON',
    PARTITIONS=4,
    TIMESTAMP='created_at',
    TIMESTAMP_FORMAT='yyyy-MM-dd HH:mm:ss'
);




INSERT INTO start_watching_events
VALUES ('session_123', 1, '2021-02-08 02:00:00');
INSERT INTO stop_watching_events
VALUES ('session_123', 1, '2021-02-08 02:01:30');
INSERT INTO start_watching_events
VALUES ('session_456', 1, '2021-02-08 02:00:00');
INSERT INTO stop_watching_events
VALUES ('session_456', 1, '2021-02-08 02:25:00');