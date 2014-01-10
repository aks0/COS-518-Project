-- @(#)19.sql	2.1.8.1
-- TPC-H/TPC-R Discounted Revenue Query (Q19)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	sum(l.extendedprice* (1 - l.discount)) as revenue
from
	lineitem l,
	part p
where
	(
		p.partkey = l.partkey
		and p.brand = ':1'
		and p.container in ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')
		and l.quantity >= :4 and l_quantity <= :4 + 10
		and p.size between 1 and 5
		and l.shipmode in ('AIR', 'AIR REG')
		and l.shipinstruct = 'DELIVER IN PERSON'
	)
	or
	(
		p.partkey = l.partkey
		and p.brand = ':2'
		and p.container in ('MED BAG', 'MED BOX', 'MED PKG', 'MED PACK')
		and l.quantity >= :5 and l_quantity <= :5 + 10
		and p.size between 1 and 10
		and l.shipmode in ('AIR', 'AIR REG')
		and l.shipinstruct = 'DELIVER IN PERSON'
	)
	or
	(
		p.partkey = l.partkey
		and p.brand = ':3'
		and p.container in ('LG CASE', 'LG BOX', 'LG PACK', 'LG PKG')
		and l.quantity >= :6 and l.quantity <= :6 + 10
		and p.size between 1 and 15
		and l.shipmode in ('AIR', 'AIR REG')
		and l.shipinstruct = 'DELIVER IN PERSON'
	);
:n -1
