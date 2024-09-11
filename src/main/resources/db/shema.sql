DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    member_id     BIGINT AUTO_INCREMENT NOT NULL,
    email         VARCHAR(100)          NOT NULL,
    member_status VARCHAR(50)           NOT NULL DEFAULT 'JOIN',
    name          VARCHAR(100),
    profile_image VARCHAR(100),
    joined_at     TIMESTAMP             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    leaved_at     TIMESTAMP,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    oauth_type     VARCHAR(50),
    oauth_id      VARCHAR(100),
    PRIMARY KEY (member_id)
);
