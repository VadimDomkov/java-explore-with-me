DROP TABLE IF EXISTS stats;

CREATE TABLE IF NOT EXISTS stats (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR(150) NOT NULL,
    uri VARCHAR(50) NOT NULL,
    ip VARCHAR(25),
    time_stamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);