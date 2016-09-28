#DDL initial sql for Database
create table WritingGroup (
    GroupName varchar(20) not null primary key,
    HeadWriter varchar(20),
    YearFormed int,
    Subject varchar(20)
);

create table Publisher (
    PublisherName varchar(20) not null primary key,
    PublisherAddress varchar(40),
    PublisherPhone varchar(15),
    PublisherEmail varchar(30)
);

create table Book (
    GroupName varchar(20) not null,
    BookTitle varchar(20) not null,
    PublisherName varchar(20) not null,
    YearPublished int,
    NumberPages int,
    constraint Book_WritingGroup_fk01 foreign key (GroupName) 
    references WritingGroup (GroupName),
    constraint Book_Publishers_fk01 foreign key (PublisherName)
    references Publisher (PublisherName),
    constraint Book_pk primary key (GroupName, BookTitle),
    constraint Book_uk01 unique (BookTitle, PublisherName)
);