CREATE SEQUENCE member_id_seq;
CREATE TABLE member (
    id   bigint DEFAULT nextval('member_id_seq') NOT NULL,
    name varchar(255),
    PRIMARY KEY (id)
);
