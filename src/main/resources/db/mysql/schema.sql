DROP TABLE IF EXISTS `membership`;

CREATE TABLE `membership`
(
    `membership_id`     BIGINT      NOT NULL AUTO_INCREMENT,
    `member_id`         BIGINT      NOT NULL,
    `squad_id`          BIGINT      NOT NULL,
    `membership_status` VARCHAR(50) NOT NULL DEFAULT 'INVITING',
    `membership_role`   VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    `created_at`        DATETIME    NULL,
    `updated_at`        DATETIME    NULL,
    PRIMARY KEY (`membership_id`)
);

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member`
(
    `member_id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `email`         VARCHAR(100) NOT NULL,
    `member_status` VARCHAR(50)  NOT NULL DEFAULT 'JOIN',
    `name`          VARCHAR(100) NULL,
    `profile_image` VARCHAR(100) NULL,
    `joined_at`     DATETIME     NOT NULL DEFAULT NOW(),
    `leaved_at`     DATETIME     NULL,
    `created_at`    DATETIME     NULL,
    `updated_at`    DATETIME     NULL,
    `oauth_type`    VARCHAR(50)  NULL,
    `oauth_id`      VARCHAR(100) NULL,
    PRIMARY KEY (`member_id`)
);

DROP TABLE IF EXISTS `squad`;

CREATE TABLE `squad`
(
    `squad_id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `squad_name`   VARCHAR(100) NOT NULL,
    `deleted_flag` TINYINT      NOT NULL DEFAULT 0,
    `created_at`   DATETIME     NULL,
    `updated_at`   DATETIME     NULL,
    PRIMARY KEY (`squad_id`)
);

ALTER TABLE `membership`
    ADD CONSTRAINT `FK_member_TO_membership_1` FOREIGN KEY (`member_id`)
        REFERENCES `member` (`member_id`);

ALTER TABLE `membership`
    ADD CONSTRAINT `FK_squad_TO_membership_1` FOREIGN KEY (`squad_id`)
        REFERENCES `squad` (`squad_id`);
