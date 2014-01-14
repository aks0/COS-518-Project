-- @(#)19.sql	2.1.8.1
-- TPC-H/TPC-R Discounted Revenue Query (Q19)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;
:n -1
