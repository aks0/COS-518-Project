SELECT users.name, item.title, item.desc
FROM users, item
WHERE item.itemid = 111
AND users.userid IN
(SELECT user_item_like.userid
FROM user_item_like
WHERE user_item_like.itemid = 111)

SELECT item.title, users.name, users.email
FROM item, users
WHERE users.userid = 123
AND item.itemid IN
(SELECT user_item_like.itemid
FROM user_item_like
WHERE user_item_like.userid = 123)