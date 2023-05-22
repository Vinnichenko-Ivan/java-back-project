create table chat (
                      id uuid not null,
                      admin_user uuid,
                      avatar_id uuid,
                      chat_type varchar(255),
                      created_date timestamp,
                      last_message_id uuid,
                      name varchar(255),
                      primary key (id)
);

create table chat_users (
                            chat_id uuid not null,
                            users uuid
);

create table message (
                         id uuid not null,
                         author_id uuid,
                         created_date timestamp,
                         text varchar(255),
                         chat_id uuid,
                         primary key (id)
);

create table message_files (
                               message_id uuid not null,
                               file_id uuid,
                               file_name varchar(255)
);

alter table if exists chat_users
    add constraint FKglok2i2m8cbulbt5xxmfqixw3
    foreign key (chat_id)
    references chat;
alter table if exists message
    add constraint FKmejd0ykokrbuekwwgd5a5xt8a
    foreign key (chat_id)
    references chat;
alter table if exists message_files
    add constraint FKif62faohiy7tl2y7789s9yxdh
    foreign key (message_id)
    references message;