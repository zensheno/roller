

-- Add to roller_comment table: approved and pending fields
    alter table roller_comment add approved bit default 1 not null;  
    alter table roller_comment add pending bit default 0 not null;  
update roller_comment set approved=1, pending=0, posttime=posttime;

-- Add to website table: commentmod, blacklist, defaultallowcomments and defaultcommentdays 
    alter table website add commentmod bit default 0 not null;  
    alter table website add defaultallowcomments bit default 1 not null;  
    alter table website add defaultcommentdays integer default 7 not null;  
    alter table website add blacklist text default null;

update website set commentmod=0, defaultallowcomments=1, defaultcommentdays=7, blacklist='', datecreated=datecreated;

-- Add weblog displaydays column
    alter table website add displaycnt integer default 15 not null;  
