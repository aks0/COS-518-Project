
-- @(#)12.sql	2.1.8.1
-- TPC-H/TPC-R Shipping Modes and Order Priority Query (Q12)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l.shipmode,
	o.orderpriority
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey;
:n -1
