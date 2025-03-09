DROP TABLE IF EXISTS notifications;

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    timeToShow TIMESTAMP,
    intervalToRepeat INTERVAL,
    user_id BIGINT NOT NULL,
    immediately BOOLEAN DEFAULT false

    CONSTRAINT type_values CHECK (type IN ('sms', 'email', 'call')),
    CONSTRAINT check_immediately_timeToShow CHECK (
        NOT (immediately = false AND timeToShow IS NULL)  -- иначе уведомление никогда не будет отправлено
    )
);

COMMENT ON TABLE notifications IS 'Таблица уведомлений';
COMMENT ON COLUMN notifications.id IS 'Идентификатор уведомления';
COMMENT ON COLUMN notifications.type IS 'Тип уведомления';
COMMENT ON COLUMN notifications.content IS 'Содержание уведомления';
COMMENT ON COLUMN notifications.timeToShow IS 'Дата и время показа первого уведомления';
COMMENT ON COLUMN notifications.intervalToRepeat IS 'Интервал, через который уведомление будет повторено';
COMMENT ON COLUMN notifications.user_id IS 'Идентификатор пользователя, которому будет отправлено уведомление';
COMMENT ON COLUMN notifications.immediately IS 'Если true, то уведомление должно быть отправлено сейчас же';

-- todo: create indexes