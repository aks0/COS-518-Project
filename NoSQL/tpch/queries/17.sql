-- @(#)17.sql	2.1.8.1
-- TPC-H/TPC-R Small-Quantity-Order Revenue Query (Q17)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	sum(l_extendedprice) as avg_yearly
from
	lineitem,
	part
where
	p_partkey = l_partkey
	and p_brand = ':1'
	and p_container = ':2';
:n -1
