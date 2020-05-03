create table card_data
(
    `key`        bigint(20) auto_increment
        primary key,
    payment_info varchar(450) not null
);


create table payment_instance
(
    `key`   bigint(20)   auto_increment
    primary key,
    card_info    varchar(300)  null,
    salt         varchar(16)    not null,
    installment  int          not null,
    price        int          null,
    tax          int          null,
    create_dt    timestamp   not null,
    update_dt    timestamp   not null
);


create table payment_detail
(
    `key`        bigint(20) auto_increment
    primary key,
    payment_key  bigint(20)  not null,
    payment_type varchar(10) not null,
    installment  int         null,
    price        int         null,
    tax          int         not null,
    create_dt    timestamp   not null,
    constraint payment_detail_payment_key_fk
        foreign key (payment_key) references payment_instance (`key`)
);

