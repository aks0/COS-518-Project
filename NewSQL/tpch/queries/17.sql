-- @(#)17.sql	2.1.8.1
-- TPC-H/TPC-R Small-Quantity-Order Revenue Query (Q17)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;
:n -1
