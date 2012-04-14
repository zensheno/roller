

-- Roller 2.4 schema changes

    alter table pingtarget add autoenabled bit default 0 not null;  

    alter table website add lastmodified datetime default null;
