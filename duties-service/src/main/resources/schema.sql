DROP TABLE IF EXISTS duties;

CREATE TABLE duties (
    id BIGSERIAL PRIMARY KEY,
    start_time TIMESTAMP NOT NULL,
    interval INTERVAL NOT NULL,
    ids BIGINT[]
);

COMMENT ON TABLE duties IS 'Таблица дежурств';
-- Схема дежурств: Round Robin: при окончании смены одного пользователя настпупает смена следующего из ids;
--                 При окончании смены последнего дежурного из ids наступает смена первого из ids; И т.д.
COMMENT ON COLUMN duties.id IS 'Идентификатор дежурства';
COMMENT ON COLUMN duties.start_time IS 'Начало дежурства';
COMMENT ON COLUMN duties.interval IS 'Сколько длится дежурство для одного пользователя';

-- todo: create indexes