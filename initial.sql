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

insert into WritingGroup(GroupName,HeadWriter,YearFormed,Subject)
VALUES ('WaterWorld','John Ham',1959,'Engrish'),
       ('FireUniverse','John Cena',1988,'Wrestling'),
       ('EarthGalaxy', 'The Rock',420,'Cooking'),
       ('WindPlanet','Steve Jobs',2001,'Film');

insert into Publisher (PublisherName, PublisherAddress, PublisherPhone, PublisherEmail)
VALUES ('Song','271 New San Fernando','210-980-1809','Song@song.com'),
       ('Hue Internation','BellFlower Brazil','520-200-3028','Hue@br.z'),
       ('Fardation','1900','1800-290-2829','lazy@fardation.com');

insert into Book (GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
VALUES ('WaterWorld','Harambe','Song',1978,370),
       ('FireUniverse','The Great Ape','Song',1965,900),
       ('EarthGalaxy','Chef Boi Are D','Hue Internation',1980,500),
       ('WindPlanet','Translating Ape','Fardation',2011,9000);

insert into Book (GroupName, BookTitle, PublisherName, YearPublished, NumberPages)
values ('EarthGalaxy','Harambe', 'Fardation', 2016, 1);