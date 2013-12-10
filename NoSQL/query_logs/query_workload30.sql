SELECT item.desc, user_item_like.itemid, user_item_like.userid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT user_item_like.userid, item.desc, item.title 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT item.desc, item.title, users.userid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.desc, users.email, item.title 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT item.desc, users.name, users.userid 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT user_item_like.itemid, user_item_like.id, item.title 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT item.desc, item.title, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.timestamp = "test")

SELECT user_item_like.itemid, item.itemid, user_item_like.userid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT item.desc, user_item_like.timestamp, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT item.title, item.desc, item.itemid 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT item.itemid, user_item_like.id, item.itemid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT item.itemid, users.name, item.title 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT users.userid, item.itemid, users.userid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT users.name, users.email, users.userid 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.itemid = "test")

SELECT item.desc, item.itemid, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.timestamp = "test")

SELECT users.userid, users.email, users.name 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT item.desc, item.desc, user_item_like.userid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT user_item_like.timestamp, users.email, users.email 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT user_item_like.userid, item.title, user_item_like.itemid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.id = "test")

SELECT item.desc, item.desc, users.name 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT users.name, item.itemid, item.desc 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT item.desc, item.title, users.userid 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.itemid = "test")

SELECT user_item_like.userid, user_item_like.id, item.itemid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT item.itemid, user_item_like.timestamp, user_item_like.id 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT item.desc, item.itemid, user_item_like.timestamp 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT users.name, user_item_like.userid, users.name 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT item.desc, item.desc, item.itemid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT users.name, users.userid, users.name 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT item.desc, users.userid, users.userid 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT users.name, item.title, item.title 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

