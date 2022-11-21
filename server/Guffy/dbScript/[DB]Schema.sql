drop database if exists guffy;
select @@global.transaction_isolation, @@transaction_isolation;
set @@transaction_isolation="read-committed";

create database guffy;
use guffy;

create table user(
	id int primary key auto_increment,
    email varchar(30) not null,
    pw varchar(20) not null,
    nickname varchar(30) not null,
    gender varchar(1) not null CHECK (gender in ("M","F","N")),
    mbti varchar(5) not null,
    interest1 varchar(20) not null,
    interest2 varchar(20) not null,
    interest3 varchar(20) not null,
    interest4 varchar(20), 
    interest5 varchar(20)
);

insert into user(email, pw, nickname, gender, mbti, interest1, interest2,interest3,interest4)  
values("je991025@gmail.com", "ssafy", "숨쉬기 운동중인 치즈냥이", 'F', "ENFJ", "산책", "강아지", "C", "요리" );
insert into user(email, pw, nickname, gender, mbti, interest1, interest2,interest3,interest4) 
values("5513019@naver.com", "ssafy", "얼음이 녹아버린 교수", 'F', "ESTP", "서핑", "고양이", "JAVA", "크로스핏" );


create table chatting_room(
	id int primary key auto_increment,
    user1_id int not null,
    user2_id int not null, 
    user1_last_visited_time long,
    user2_last_visited_time long,
    user1_last_chatting_time long,
    user2_last_chatting_time long,
    constraint fk_user1_id foreign key (user1_id) references user(id) on delete cascade,
    constraint fk_user2_id foreign key (user2_id) references user(id) on delete cascade
    );
    
insert into chatting_room (user1_id, user2_id) values (1, 2);

create table friends_num(
	id int primary key auto_increment,
    user_id int not null,
    friends_num int default 0,
    constraint fk_user_id foreign key (user_id) references user(id) on delete cascade
);

insert into friends_num(user_id, friends_num)
value(1, 1);

insert into friends_num(user_id, friends_num)
value(2, 2);



select * from user;
select * from friends_num;
select * from chatting_room;

select *
from user u join chatting_room c
on u.id in (c.user1_id, c.user2_id)
where u.id = 1;

