-- @(#)16.sql	2.1.8.1
-- TPC-H/TPC-R Parts/Supplier Relationship Query (Q16)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	p.brand,
	p.type,
	p.size,
	ps.suppkey
from
	partsupp ps,
	part p
where
	p.partkey = ps.partkey;
:n -1
