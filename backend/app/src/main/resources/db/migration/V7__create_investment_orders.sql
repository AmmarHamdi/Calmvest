CREATE TABLE investment_orders (
    id                  UUID         NOT NULL PRIMARY KEY,
    user_id             UUID         NOT NULL REFERENCES users(id),
    goal_id             UUID         NOT NULL REFERENCES goals(id),
    amount_minor_units  BIGINT       NOT NULL,
    mode                VARCHAR(20)  NOT NULL,
    status              VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    idempotency_key     VARCHAR(255) NOT NULL UNIQUE,
    provider_order_id   VARCHAR(255),
    created_at          TIMESTAMPTZ  NOT NULL,
    executed_at         TIMESTAMPTZ,
    updated_at          TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_investment_orders_user_id      ON investment_orders (user_id);
CREATE INDEX idx_investment_orders_goal_id      ON investment_orders (goal_id);
CREATE INDEX idx_investment_orders_idempotency  ON investment_orders (idempotency_key);
CREATE INDEX idx_investment_orders_status       ON investment_orders (status);
