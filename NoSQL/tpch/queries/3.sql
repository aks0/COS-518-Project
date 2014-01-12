-- @(#)3.sql	2.1.8.1
-- TPC-H/TPC-R Shipping Priority Query (Q3)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l.orderkey,
	l.extendedprice,
	l.discount,
	o.orderdate,
	o.shippriority
from
	customer c,
	orders o,
	lineitem l
where
	c.custkey = o.custkey
	l.orderkey = o.orderkey;
:n 10
