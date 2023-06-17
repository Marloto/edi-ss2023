INSERT INTO production_changes VALUES (
    '1', 1, 1, 'season_length',
    STRUCT(season_id := 1, episode_count := 12),
    STRUCT(season_id := 1, episode_count := 8),
    '2021-02-24 10:00:00'
);
INSERT INTO production_changes VALUES (
    '1', 1, 1, 'season_length',
    STRUCT(season_id := 1, episode_count := 8),
    STRUCT(season_id := 1, episode_count := 10),
    '2021-02-24 11:00:00'
);
INSERT INTO production_changes VALUES (
    '1', 1, 1, 'season_length',
    STRUCT(season_id := 1, episode_count := 10),
    STRUCT(season_id := 1, episode_count := 8),
    '2021-02-24 10:59:00'
);
INSERT INTO production_changes VALUES (
    '1', 1, 1, 'season_length',
    STRUCT(season_id := 1, episode_count := 8),
    STRUCT(season_id := 1, episode_count := 12),
    '2021-02-24 11:10:00'
);
INSERT INTO production_changes VALUES (
    '1', 1, 1, 'season_length',
    STRUCT(season_id := 1, episode_count := 12),
    STRUCT(season_id := 1, episode_count := 8),
    '2021-02-24 10:59:00'
);