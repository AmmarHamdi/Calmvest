CREATE TABLE portfolios (
    id                          UUID        NOT NULL PRIMARY KEY,
    user_id                     UUID        NOT NULL REFERENCES users(id),
    mode                        VARCHAR(20) NOT NULL,
    total_invested_minor_units  BIGINT      NOT NULL DEFAULT 0,
    current_value_minor_units   BIGINT      NOT NULL DEFAULT 0,
    created_at                  TIMESTAMPTZ NOT NULL,
    updated_at                  TIMESTAMPTZ NOT NULL,
    UNIQUE (user_id, mode)
);

CREATE INDEX idx_portfolios_user_id ON portfolios (user_id);
