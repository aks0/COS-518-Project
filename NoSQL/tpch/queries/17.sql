-- @(#)17.sql	2.1.8.1
-- TPC-H/TPC-R Small-Quantity-Order Revenue Query (Q17)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	sum(l.extendedprice) as avg_yearly
from
	lineitem l,
	part p
where
	p.partkey = l.partkey
	and p.brand = ':1'
	and p.container = ':2';
:n -1
