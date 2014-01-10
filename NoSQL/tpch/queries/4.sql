-- @(#)4.sql	2.1.8.1
-- TPC-H/TPC-R Order Priority Checking Query (Q4)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	o.orderpriority,
	count(*) as order_count
from
	orders o
where
	o.orderdate >= ':1'
group by
	o.orderpriority
order by
	o.orderpriority;
:n -1
