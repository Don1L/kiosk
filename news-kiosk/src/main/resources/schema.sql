CREATE TABLE IF NOT EXISTS publications (
    id          BIGSERIAL    PRIMARY KEY,
    type        VARCHAR(20)  NOT NULL,
    title       VARCHAR(255) NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    quantity    INT          NOT NULL DEFAULT 0,
    issue_number INT,
    pub_date    VARCHAR(10),
    month_year  VARCHAR(7),
    author      VARCHAR(255),
    isbn        VARCHAR(20)
);
