
-- @(#)12.sql	2.1.8.1
-- TPC-H/TPC-R Shipping Modes and Order Priority Query (Q12)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l.shipmode,
	sum(case
		when o.orderpriority = '1-URGENT'
			or o.orderpriority = '2-HIGH'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o.orderpriority <> '1-URGENT'
			and o.orderpriority <> '2-HIGH'
			then 1
		else 0
	end) as low_line_count
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey
	and l.shipmode in (':1', ':2')
	and l.commitdate < l.receiptdate
	and l.shipdate < l.commitdate
	and l.receiptdate >= ':3'
group by
	l.shipmode
order by
	l.shipmode;
:n -1
