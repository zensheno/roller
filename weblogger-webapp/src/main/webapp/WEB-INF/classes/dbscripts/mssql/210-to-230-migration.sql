

-- Roller 2.2 schema changes

    alter table website add isactive bit default 1 not null;  
update website set isactive=1, datecreated=datecreated;

-- Roller 2.3 schema changes

    alter table weblogentry add summary text default null;
    alter table weblogentry add content_type varchar(48) default null;
    alter table weblogentry add content_src varchar(255) default null;


