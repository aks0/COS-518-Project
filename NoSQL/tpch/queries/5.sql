
-- @(#)5.sql	2.1.8.1
-- TPC-H/TPC-R Local Supplier Volume Query (Q5)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	n.name,
	l.extendedprice,
	l.discount
from
	customer c,
	orders o,
	lineitem l,
	supplier s,
	nation n,
	region r
where
	c.custkey = o.custkey
	and l.orderkey = o.orderkey
	and l.suppkey = s.suppkey
	and c.nationkey = s.nationkey
	and s.nationkey = n.nationkey
	and n.regionkey = r.regionkey;
:n -1
