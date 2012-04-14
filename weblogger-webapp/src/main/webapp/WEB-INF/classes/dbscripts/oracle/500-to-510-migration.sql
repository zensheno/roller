

create table rol_weblogtheme (
    id              varchar(48)  not null primary key,
    weblogid varchar(48) not null,
    name            varchar(255)  not null,
    custom          number(1) default 0 not null,
       type      varchar(16) default 'standard' not null
);

create table rol_templatecode (
    id                 varchar(48)  not null primary key,
    templateid varchar(48) not null,
    template     clob not null,
    templatelang varchar(48),
    contenttype  varchar(48),
       type      varchar(16) default 'standard' not null
);

    alter table webpage add type varchar(16) default null;


