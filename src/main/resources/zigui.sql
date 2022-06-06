CREATE EXTENSION postgis;
CREATE EXTENSION postgis_raster;
CREATE EXTENSION fuzzystrmatch;
CREATE EXTENSION postgis_tiger_geocoder;
CREATE EXTENSION postgis_topology;
create table t_user
(
    user_id      varchar(32)  default ''::character varying     not null
        primary key,
    username     varchar(32)  default ''::character varying     not null,
    password     varchar(128) default ''::character varying     not null,
    role         varchar(16)  default 'USER'::character varying not null,
    deleted      boolean      default false                     not null,
    created_time timestamp    default now()
);

alter table t_user
    owner to postgres;

create table t_user_info
(
    user_id      varchar(32) default ''::character varying not null
        primary key,
    name         varchar(32) default ''::character varying not null,
    last_login   timestamp   default now(),
    deleted      boolean     default false                 not null,
    created_time timestamp   default now(),
    work_number  varchar(32) default ''::character varying not null,
    post         varchar(32) default ''::character varying not null,
    mobile       varchar(16) default ''::character varying not null
);

alter table t_user_info
    owner to postgres;

create table t_route
(
    route_id       varchar(32)  default ''::character varying not null
        primary key,
    route_name     varchar(128) default ''::character varying not null,
    route_desc     text,
    route_polyline geometry,
    deleted        boolean      default false                 not null,
    created_time   timestamp    default now(),
    saved          boolean      default false                 not null,
    content        text         default ''::text              not null
);

alter table t_route
    owner to postgres;

create table t_route_point
(
    point_id       varchar(32)  default ''::character varying not null
        primary key,
    point_name     varchar(128) default ''::character varying not null,
    point_desc     text,
    point_location geometry,
    deleted        boolean      default false                 not null,
    created_time   timestamp    default now()                 not null,
    route_id       varchar(32)  default ''::character varying not null,
    content        text         default ''::text              not null
);

alter table t_route_point
    owner to postgres;

create table t_file
(
    file_id       varchar(32)  default ''::character varying not null
        constraint t_resource_pkey
        primary key,
    original_name varchar(128) default ''::character varying not null,
    local_name    varchar(128) default ''::character varying not null,
    deleted       boolean      default false                 not null,
    created_time  timestamp    default now()                 not null
);

alter table t_file
    owner to postgres;

create table spatial_ref_sys
(
    srid      integer not null
        primary key
        constraint spatial_ref_sys_srid_check
        check ((srid > 0) AND (srid <= 998999)),
    auth_name varchar(256),
    auth_srid integer,
    srtext    varchar(2048),
    proj4text varchar(2048)
);

alter table spatial_ref_sys
    owner to postgres;

grant select on spatial_ref_sys to public;

create table t_resource
(
    resource_id   varchar(32) default ''::character varying not null
        constraint t_resource_pk
        primary key,
    file_id       varchar(32) default ''::character varying not null,
    resource_type varchar(32) default ''::character varying not null,
    addition      text,
    deleted       boolean     default false                 not null,
    created_time  timestamp   default now()
);

alter table t_resource
    owner to postgres;

create table t_theme
(
    theme_id     serial
        constraint t_theme_pk
        primary key,
    theme_name   varchar(128) default ''::character varying not null,
    theme_desc   text         default ''::text              not null,
    deleted      boolean      default false                 not null,
    created_time timestamp    default now()                 not null
);

alter table t_theme
    owner to postgres;

create unique index t_theme_theme_id_uindex
    on t_theme (theme_id);

create unique index t_theme_theme_name_uindex
    on t_theme (theme_name);

create table t_route_theme
(
    route_theme_id varchar(64) default ''::character varying not null
        constraint t_route_theme_pk
        primary key,
    route_id       varchar(32) default ''::character varying not null,
    theme_id       integer     default 0                     not null,
    deleted        boolean     default false                 not null,
    created_time   timestamp   default now()                 not null
);

alter table t_route_theme
    owner to postgres;

create table t_user_wx
(
    open_id      varchar(32) default ''::character varying not null
        constraint t_user_wx_pk
        primary key,
    user_id      varchar(32) default ''::character varying not null,
    nick_name    varchar(64) default ''::character varying not null,
    gender       smallint    default 0                     not null,
    country      varchar(64) default ''::character varying not null,
    province     varchar(64) default ''::character varying not null,
    city         varchar(64) default ''::character varying not null,
    deleted      boolean     default false                 not null,
    created_time timestamp   default now()                 not null,
    mobile       varchar(16) default ''::character varying not null
);

alter table t_user_wx
    owner to postgres;



