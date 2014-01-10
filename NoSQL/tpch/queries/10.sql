-- @(#)10.sql	2.1.8.1
-- TPC-H/TPC-R Returned Item Reporting Query (Q10)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	c.custkey,
	c.name,
	sum(l.extendedprice * (1 - l.discount)) as revenue,
	c.acctbal,
	n.name,
	c.address,
	c.phone,
	c.comment
from
	customer c,
	orders o,
	lineitem l,
	nation n
where
	c.custkey = o.custkey
	and l.orderkey = o.orderkey
	and o.orderdate >= ':1'
	and l.returnflag = 'R'
	and c.nationkey = n.nationkey
group by
	c.custkey,
	c.name,
	c.acctbal,
	c.phone,
	n.name,
	c.address,
	c.comment
order by
	revenue desc;
:n 20
