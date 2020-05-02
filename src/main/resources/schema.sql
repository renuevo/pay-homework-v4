create table card_data
(
    `key`        int auto_increment
        primary key,
    payment_info varchar(450) not null
);

create table payment
(
    `key`        bigint(20)   auto_increment primary key,
    installment  int          not null,
    price        int          null,
    tax          int          null,
    payment_info varchar(450) not null
);

create table payment_detail
(
    `key`        bigint(20) auto_increment primary key,
    payment_key  long      not null,
    payment_type varchar(10) not null,
    installment  int         null,
    price        int         null,
    tax          int         not null,
    constraint payment_detail_payment_key_fk
        foreign key (payment_key) references payment (`key`)
);

