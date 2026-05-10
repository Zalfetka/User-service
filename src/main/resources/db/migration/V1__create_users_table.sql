CREATE TABLE hibernate_sequence (
                                    next_val BIGINT
);

INSERT INTO hibernate_sequence (next_val) VALUES (1);
INSERT INTO hibernate_sequence (next_val) VALUES (1);

CREATE TABLE user1 (
                id BIGSERIAL PRIMARY KEY,
                password VARCHAR(64) NOT NULL,
                username VARCHAR(64) NOT NULL UNIQUE,
                weight INTEGER NOT NULL,
                height INTEGER NOT NULL,
                age SMALLINT NOT NULL,
                gender VARCHAR(64) NOT NULL,
                activity VARCHAR(64) NOT NULL,
                role VARCHAR(64) NOT NULL,
                caloriesNorm INTEGER NOT NULL,
                proteinNorm DECIMAL NOT NULL,
                carbsNorm DECIMAL NOT NULL,
                fatNorm DECIMAL NOT NULL
);

CREATE TABLE food (
                id BIGSERIAL PRIMARY KEY,
                dailyNorm INTEGER NOT NULL,
                consumedCalories INTEGER NOT NULL,
                remainingCalorie INTEGER NOT NULL,
                date DATE  NOT NULL,
                proteinNorm DECIMAL NOT NULL,
                carbsNorm DECIMAL NOT NULL,
                fatNorm DECIMAL NOT NULL

);

CREATE TABLE kafka_message_log (
                id BIGSERIAL PRIMARY KEY,
                payload TEXT NOT NULL,
                status VARCHAR NOT NULL,
                sent_at TIMESTAMP NOT NULL,
                updated_at TIMESTAMP NOT NULL
);