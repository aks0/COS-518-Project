-- @(#)3.sql	2.1.8.1
-- TPC-H/TPC-R Shipping Priority Query (Q3)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l.orderkey,
	sum(l.extendedprice * (1 - l.discount)) as revenue,
	o.orderdate,
	o.shippriority
from
	customer c,
	orders o,
	lineitem l
where
	c.mktsegment = ':1'
	and c.custkey = o.custkey
	and l.orderkey = o.orderkey
	and o.orderdate < ':2'
	and l.shipdate > ':2'
group by
	l.orderkey,
	o.orderdate,
	o.shippriority
order by
	revenue desc,
	o.orderdate;
:n 10
