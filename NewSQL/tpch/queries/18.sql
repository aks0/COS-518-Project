-- @(#)18.sql	2.1.8.1
-- TPC-H/TPC-R Large Volume Customer Query (Q18)
-- Function Query Definition
-- Approved February 1998
:x
:o
select
	c.name,
	c.custkey,
	o.orderkey,
	o.orderdate,
	o.totalprice,
	l.quantity
from
	customer c,
	orders o,
	lineitem l
where
	c.custkey = o.custkey
	and o.orderkey = l.orderkey;
:n 100
