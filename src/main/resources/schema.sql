CREATE TABLE RESERVATION
(
    id       bigint not null auto_increment,
    theme_id bigint,
    date     date,
    time     time,
    name     varchar(20),
    primary key (id)
);

CREATE TABLE theme
(
    id    bigint not null auto_increment,
    name  varchar(20),
    desc  varchar(255),
    price int,
    primary key (id)
);