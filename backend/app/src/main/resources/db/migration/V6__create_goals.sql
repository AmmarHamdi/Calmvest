CREATE TABLE goals (
    id                          UUID         NOT NULL PRIMARY KEY,
    user_id                     UUID         NOT NULL REFERENCES users(id),
    name                        VARCHAR(255) NOT NULL,
    description                 TEXT,
    target_amount_minor_units   BIGINT       NOT NULL,
    current_amount_minor_units  BIGINT       NOT NULL DEFAULT 0,
    target_date                 DATE,
    status                      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at                  TIMESTAMPTZ  NOT NULL,
    updated_at                  TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_goals_user_id ON goals (user_id);
CREATE INDEX idx_goals_status  ON goals (status);
