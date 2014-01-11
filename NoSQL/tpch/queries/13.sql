-- @(#)21.sql	2.1.8.1
-- TPC-H/TPC-R Suppliers Who Kept Orders Waiting Query (Q21)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	s.name,
    count(*)
from
	supplier s,
	lineitem l1,
	orders o,
	nation n
where
	s.suppkey = l1.suppkey
	and o.orderkey = l1.orderkey
	and o.orderstatus = 'F'
	and l1.receiptdate > l1.commitdate
	and s.nationkey = n.nationkey
	and n.name = ':1'
group by
	s.name
order by
	s.name;
:n 100
