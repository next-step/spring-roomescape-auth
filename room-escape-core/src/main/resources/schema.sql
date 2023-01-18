drop table if exists reservation;
drop table if exists themes;
drop table if exists schedules;
drop table if exists member;

create table reservation
(
    id          bigint not null primary key auto_increment comment '예약 식별자',
    schedule_id bigint not null comment '스케줄 식별자',
    member_id   bigint not null comment '예약자 아이디'
);

create table themes
(
    id    bigint       not null primary key auto_increment comment '테마 식별자',
    name  varchar(32)  not null comment '테마 이름',
    desc  varchar(128) not null comment '테마 설명',
    price bigint       not null comment '테마 가격'
);

create table schedules
(
    id       bigint not null primary key auto_increment comment '스케줄 식별자',
    theme_id bigint not null comment '테마 식별자',
    date     date   not null default now() comment '스케줄 날짜',
    time     time   not null default now() comment '스케줄 시간'
);

create table member
(
    id       bigint      not null primary key auto_increment comment '회원 식별자',
    username varchar(20) not null comment '회원 아이디',
    password varchar(20) not null comment '회원 비밀번호',
    name     varchar(20) not null comment '회원 이름',
    role     varchar(20) not null comment '회원 구분',
    phone    varchar(20) not null comment '회원 전화번호'
);