/* 카드사 Table */
create table card_company
(
    `key`        int auto_increment comment 'key'
        primary key,
    payment_info varchar(450) not null comment '카드사 통신 정보'
);


/* 카드 상태 정보 Table */
create table card_info
(
    `key`        int auto_increment comment 'key'
        primary key,
    card_number  varchar (8)    not null comment '암호화 카드 번호 정보',
    card_info    varchar(300)  not null  comment '암호화 카드 정보',
    use_status boolean not null comment '카드 사용여부'
);


/* 결제 현황 Table */
create table payment_instance
(
    `key`   int   auto_increment comment 'key'
    primary key,
    identity_number varchar (20)  not null unique  comment '관리번호',
    cancel_identity_number varchar (20)  not null unique comment '취소 관리번호',
    cancel       boolean        default false not null comment '전체 취소 여부',
    card_info    varchar(300)  null comment '암호화 카드 정보',
    salt         varchar(16)    not null comment '암호화 salt',
    installment  int          not null comment '할부개월',
    price        int          null comment '금액',
    tax          int          null comment '부가가치세',
    create_dt    timestamp   not null comment '최초 결제일',

    constraint payment_instance_card_info_key_fk
        foreign key (card_info) references card_info (`card_info`)
);


/* 결제 상세 내역 현황 Table */
create table payment_detail
(
    `key`        int auto_increment  comment 'key'
    primary key,
    identity_number varchar (20)  not null  comment '관리번호',
    payment_type varchar(10) not null  comment '결제/취소',
    installment  int         null  comment '할부개월',
    price        int         null  comment '금액',
    tax          int         not null  comment '부가가치세',
    create_dt    timestamp   not null  comment '내역 생성일',

    constraint payment_detail_identity_key_fk
        foreign key (identity_number) references payment_instance (`identity_number`)
);


/* 카드 조회 Index */
create unique index search_card_index on card_info (card_number);

/* 카드 내역 조회 Index */
create unique index search_detail_index on payment_detail (identity_number, create_dt DESC);