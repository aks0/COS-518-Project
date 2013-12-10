SELECT item.itemid, item.itemid, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT item.title, item.title, item.desc 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT users.email, users.userid, item.desc 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT users.email, user_item_like.userid, user_item_like.timestamp 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT item.itemid, users.userid, users.userid 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT item.itemid, item.title, user_item_like.timestamp 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT item.title, item.itemid, user_item_like.userid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT user_item_like.itemid, item.title, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT users.name, users.name, users.email 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.title, users.userid, users.userid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT item.title, item.title, item.desc 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT users.name, users.userid, user_item_like.userid 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT user_item_like.userid, users.email, user_item_like.itemid 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT item.itemid, users.userid, item.title 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.timestamp = "test")

SELECT user_item_like.id, user_item_like.userid, user_item_like.id 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT item.title, item.desc, users.email 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT users.name, user_item_like.id, user_item_like.userid 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT item.desc, item.itemid, users.userid 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT users.userid, users.userid, users.userid 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

SELECT users.email, users.email, users.email 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT item.title, users.email, users.userid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT user_item_like.timestamp, user_item_like.timestamp, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.timestamp = "test")

SELECT user_item_like.itemid, item.itemid, item.itemid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT item.itemid, item.desc, item.desc 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.title, item.itemid, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT item.itemid, users.name, item.title 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.id = "test")

SELECT users.name, users.userid, user_item_like.timestamp 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.desc, item.title, users.userid 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT users.email, users.userid, users.email 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT user_item_like.id, users.email, users.name 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT users.email, users.name, item.desc 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT item.desc, item.itemid, item.title 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.title = "test")

SELECT item.itemid, item.title, item.title 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.desc, item.desc, user_item_like.id 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT users.userid, users.userid, users.email 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT user_item_like.id, user_item_like.id, users.email 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT users.name, item.itemid, users.name 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT item.title, item.title, users.email 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.id = "test")

SELECT item.itemid, item.desc, users.name 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT item.desc, users.email, users.userid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT user_item_like.itemid, user_item_like.itemid, users.userid 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT users.name, users.name, item.desc 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.id = "test")

SELECT item.title, item.title, item.desc 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT users.name, users.userid, item.title 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT users.name, item.title, users.name 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT item.desc, users.email, item.desc 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT item.desc, user_item_like.id, item.itemid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT item.itemid, user_item_like.timestamp, user_item_like.userid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.itemid = "test")

SELECT item.title, users.userid, users.name 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.itemid = "test")

SELECT users.userid, user_item_like.timestamp, user_item_like.userid 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT users.email, item.title, users.userid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.itemid, item.itemid, item.desc 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.itemid = "test")

SELECT item.title, item.title, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT users.email, users.name, user_item_like.userid 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT users.name, item.itemid, users.name 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT user_item_like.itemid, users.userid, users.userid 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.userid = "test")

SELECT item.title, users.email, item.title 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.timestamp = "test")

SELECT users.email, users.name, users.userid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT user_item_like.id, user_item_like.id, user_item_like.timestamp 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT item.itemid, item.itemid, item.itemid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.id = "test")

SELECT item.desc, users.userid, item.itemid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT users.name, users.userid, user_item_like.id 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT users.name, users.userid, users.userid 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT user_item_like.userid, item.title, user_item_like.id 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.userid = "test")

SELECT item.title, users.userid, item.itemid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.desc, users.userid, item.title 
FROM item, users 
WHERE item.itemid IN 
(SELECT user_item_like.itemid 
FROM user_item_like 
WHERE user_item_like.id = "test")

SELECT user_item_like.itemid, users.name, users.email 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT user_item_like.itemid, item.title, user_item_like.itemid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT users.userid, user_item_like.userid, user_item_like.id 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT user_item_like.timestamp, user_item_like.id, users.email 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.name = "test")

SELECT item.title, users.name, item.itemid 
FROM item, users 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT user_item_like.itemid, users.email, users.name 
FROM users, user_item_like 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.itemid, item.desc, item.itemid 
FROM users, item 
WHERE users.userid IN 
(SELECT users.userid 
FROM users 
WHERE users.email = "test")

SELECT item.desc, user_item_like.userid, item.desc 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.itemid = "test")

SELECT user_item_like.userid, user_item_like.itemid, item.itemid 
FROM item, user_item_like 
WHERE item.itemid IN 
(SELECT item.itemid 
FROM item 
WHERE item.desc = "test")

