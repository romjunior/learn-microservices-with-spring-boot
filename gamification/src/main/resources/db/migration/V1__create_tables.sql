create table if not exists badge_card (
    badge_id bigserial not null,
    user_id bigint not null,
    badge_timestamp bigint not null,
    badge varchar(100) not null,
    CONSTRAINT pk_badge_card PRIMARY KEY(badge_id)
);

create table if not exists score_card (
    card_id bigserial not null,
    user_id bigint not null,
    attempt_id bigint not null,
    score_ts bigint not null,
    score bigint not null,
    CONSTRAINT pk_score_card PRIMARY KEY(card_id)
);