CREATE TABLE reserve_entries (
    id                  UUID         NOT NULL PRIMARY KEY,
    user_id             UUID         NOT NULL REFERENCES users(id),
    transaction_id      UUID         NOT NULL REFERENCES transactions(id),
    amount_minor_units  BIGINT       NOT NULL,
    created_at          TIMESTAMPTZ  NOT NULL,
    idempotency_key     VARCHAR(255) NOT NULL UNIQUE
);

CREATE INDEX idx_reserve_entries_user_id      ON reserve_entries (user_id);
CREATE INDEX idx_reserve_entries_idempotency  ON reserve_entries (idempotency_key);
