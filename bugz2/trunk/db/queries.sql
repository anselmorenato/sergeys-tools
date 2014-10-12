select * from visit
where http_user_agent like '%googlebot%'

delete from visit
where http_user_agent like '%googlebot%'
or http_user_agent like '%yandexbot%'
or http_user_agent like '%bingbot%'
or http_user_agent like '%mail.ru_bot%'
