

create table rol_weblogtheme (
    id              varchar(48)  not null primary key,
    weblogid varchar(48) not null,
    name            varchar(255)  not null,
    custom          smallint default 0 not null,
       type      varchar(16) not null default 'standard'
);

create table rol_templatecode (
    id                 varchar(48)  not null primary key,
    templateid varchar(48) not null,
    template     clob(102400) not null,
    templatelang varchar(48),
    contenttype  varchar(48),
       type      varchar(16) not null default 'standard'
);

    alter table webpage add column type varchar(16) default null;


