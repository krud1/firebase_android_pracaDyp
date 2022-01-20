create table projects
(
    id              int primary key auto_increment,
    description     varchar(100) not null
);

alter table TASK_GROUPS add column project_id int null;
alter table TASK_GROUPS add foreign key (project_id) references projects (id);

create table project_steps
(
    id                  int primary key auto_increment,
    description         varchar(100) not null,
    days_to_deadline    datetime null
);

alter table project_steps add column project_id int null;
alter table project_steps add foreign key (project_id) references projects (id);