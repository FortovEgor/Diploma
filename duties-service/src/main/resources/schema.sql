-- DROP TABLE IF EXISTS duties;

CREATE TABLE IF NOT EXISTS duties (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    interval BIGINT NOT NULL,  -- seconds
    ids BIGINT[]
);

COMMENT ON TABLE duties IS 'Таблица дежурств';
-- Схема дежурств: Round Robin: при окончании смены одного пользователя настпупает смена следующего из ids;
--                 При окончании смены последнего дежурного из ids наступает смена первого из ids; И т.д.
COMMENT ON COLUMN duties.id IS 'Идентификатор дежурства';
COMMENT ON COLUMN duties.start_time IS 'Начало дежурства';
COMMENT ON COLUMN duties.interval IS 'Сколько длится дежурство для одного пользователя';
COMMENT ON COLUMN duties.ids IS 'id пользователей, которым назначено это дежурство'

-- todo: create indexes