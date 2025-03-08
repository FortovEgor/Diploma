DROP TABLE IF EXISTS users;

CREATE TABLE duty (
    id BIGSERIAL PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    interval INTERVAL NOT NULL,
    ids BIGINT[]
);

COMMENT ON TABLE duty IS 'Таблица дежурств';
-- Схема дежурств: Round Robin: при окончании смены одного пользователя настпупает смена следующего из ids;
--                 При окончании смены последнего дежурного из ids наступает смена первого из ids; И т.д.
COMMENT ON COLUMN duty.id IS 'Идентификатор дежурства';
COMMENT ON COLUMN duty.start_time IS 'Начало дежурства';
COMMENT ON COLUMN duty.interval IS 'Сколько длится дежурство для одного пользователя';

-- todo: create indexes