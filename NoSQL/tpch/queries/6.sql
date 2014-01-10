-- @(#)6.sql	2.1.8.1
-- TPC-H/TPC-R Forecasting Revenue Change Query (Q6)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	sum(l.extendedprice * l.discount) as revenue
from
	lineitem l
where
	l.shipdate >= ':1'
	and l.discount between :2 - 0.01 and :2 + 0.01
	and l.quantity < :3;
:n -1
