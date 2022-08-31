CREATE TABLE RESERVATION
(
    id        bigint not null auto_increment,
    date     date,
    time     time,
    name      varchar(20),
    primary key (id)
);