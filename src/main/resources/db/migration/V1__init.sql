drop table if exists TASKS;
CREATE TABLE tasks(
    id int primary key auto_increment,
    description varchar(100) not null,
    done bit
)