CREATE TABLE round_up_rules (
    id                                      UUID         NOT NULL PRIMARY KEY,
    user_id                                 UUID         NOT NULL UNIQUE REFERENCES users(id),
    is_enabled                              BOOLEAN      NOT NULL DEFAULT FALSE,
    monthly_cap_minor_units                 BIGINT       NOT NULL,
    current_month_accumulated_minor_units   BIGINT       NOT NULL DEFAULT 0,
    paused_until                            TIMESTAMPTZ,
    investment_threshold_minor_units        BIGINT       NOT NULL,
    created_at                              TIMESTAMPTZ  NOT NULL,
    updated_at                              TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_round_up_rules_user_id ON round_up_rules (user_id);
