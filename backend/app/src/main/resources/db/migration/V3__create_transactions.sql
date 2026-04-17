CREATE TABLE transactions (
    id                          UUID         NOT NULL PRIMARY KEY,
    bank_account_id             UUID         NOT NULL REFERENCES bank_accounts(id),
    user_id                     UUID         NOT NULL REFERENCES users(id),
    amount_minor_units          BIGINT       NOT NULL,
    round_up_amount_minor_units BIGINT       NOT NULL,
    description                 TEXT         NOT NULL,
    merchant_name               VARCHAR(255),
    transacted_at               TIMESTAMPTZ  NOT NULL,
    imported_at                 TIMESTAMPTZ  NOT NULL,
    idempotency_key             VARCHAR(255) NOT NULL UNIQUE
);

CREATE INDEX idx_transactions_user_id        ON transactions (user_id);
CREATE INDEX idx_transactions_bank_account   ON transactions (bank_account_id);
CREATE INDEX idx_transactions_idempotency    ON transactions (idempotency_key);
