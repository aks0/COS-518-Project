-- @(#)14.sql	2.1.8.1
-- TPC-H/TPC-R Promotion Effect Query (Q14)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	sum(case
		when p.type like 'PROMO%'
			then l.extendedprice * (1 - l.discount)
		else 0
	end) as promo_revenue,
	sum(l.extendedprice * (1 - l.discount))
from
	lineitem l,
	part p
where
	l.partkey = p.partkey
	and l.shipdate >= ':1'
:n -1
