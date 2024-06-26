DROP TABLE IF EXISTS users, categories, locations, events, compilations, compilation_to_event, requests, comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    email VARCHAR UNIQUE NOT NULL,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation VARCHAR NOT NULL,
    cat_id BIGINT NOT NULL REFERENCES categories(id) ON UPDATE CASCADE ON DELETE CASCADE,
    confirmed_requests INTEGER,
    created_on TIMESTAMP,
    description VARCHAR,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator BIGINT NOT NULL REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    location BIGINT NOT NULL REFERENCES locations(id) ON UPDATE CASCADE ON DELETE CASCADE,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER DEFAULT 0,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT TRUE,
    state VARCHAR,
    title VARCHAR NOT NULL,
    views BIGINT
    );

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR
);

CREATE TABLE IF NOT EXISTS compilation_to_event (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compilation_id BIGINT NOT NULL REFERENCES compilations(id) ON UPDATE CASCADE ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events(id) ON UPDATE CASCADE ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requester_id BIGINT NOT NULL REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events(id) ON UPDATE CASCADE ON DELETE CASCADE,
    created TIMESTAMP NOT NULL,
    status VARCHAR NOT NULL
    );

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES events(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    text VARCHAR(2000) NOT NULL,
    evaluation VARCHAR(15) NOT NULL,
    published TIMESTAMP NOT NULL
    );