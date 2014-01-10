-- @(#)1.sql	2.1.8.1
-- TPC-H/TPC-R Pricing Summary Report Query (Q1)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l.returnflag,
	l.linestatus,
	sum(l.quantity) as sum_qty,
	sum(l.extendedprice) as sum_base_price,
	sum(l.extendedprice * (1 - l.discount)) as sum_disc_price,
	sum(l.extendedprice * (1 - l.discount) * (1 + l.tax)) as sum_charge,
	avg(l.quantity) as avg_qty,
	avg(l.extendedprice) as avg_price,
	avg(l.discount) as avg_disc,
	count(*) as count_order
from
	lineitem l
where
	l.orderkey <= ':1'
group by
	l.returnflag,
	l.linestatus
order by
	l.returnflag,
	l.linestatus;
:n -1
