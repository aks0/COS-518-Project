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
	count(distinct ps.suppkey) as supplier_cnt
from
	partsupp ps,
	part p
where
	p.partkey = ps.partkey
	and p.brand <> ':1'
	and p.type not like ':2%'
	and p.size in (:3, :4, :5, :6, :7, :8, :9, :10)
group by
	p.brand,
	p.type,
	p.size
order by
	supplier_cnt desc,
	p.brand,
	p.type,
	p.size;
:n -1
