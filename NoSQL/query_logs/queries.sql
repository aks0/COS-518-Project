SELECT a.name, b.title, b.desc
FROM users AS a, item AS b
WHERE b.itemid = 111
AND a.userid IN
(SELECT userid
FROM user_item_like
WHERE itemid = 111)

SELECT a.title, b.name, b.email
FROM item AS a, users AS b
WHERE a.userid = 123
AND b.itemid IN
(SELECT itemid
FROM user_item_like
WHERE userid = 123)