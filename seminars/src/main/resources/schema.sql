CREATE TABLE IF NOT EXISTS user_account (
    id SERIAL PRIMARY KEY,
    login VARCHAR(80) UNIQUE NOT NULL,
    name VARCHAR(80) NOT NULL,
    password VARCHAR(80) NOT NULL,
    my_role varchar(10) NOT NULL DEFAULT 'User'
);

CREATE TABLE IF NOT EXISTS location (
    id SERIAL PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    open_time TIME,
    close_time TIME,
    max_people INT NOT NULL,
    owner INT,
    FOREIGN KEY (owner) REFERENCES user_account (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event (
    id SERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    date_time DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    ticket_cost INT NOT NULL,
    peoples_count INT,
    location_id INT NOT NULL,
    owner INT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location (id) ON DELETE CASCADE,
    FOREIGN KEY (owner) REFERENCES user_account (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_event (
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    PRIMARY KEY (user_id, event_id),
    FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS location_request (
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    date_time DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    owner INT NOT NULL,
    approved BOOLEAN NOT NULL DEFAULT 'false',
    FOREIGN KEY (owner) REFERENCES user_account (id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES location (id) ON DELETE CASCADE
);