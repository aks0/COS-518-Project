-- @(#)6.sql	2.1.8.1
-- TPC-H/TPC-R Forecasting Revenue Change Query (Q6)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	l.extendedprice,
	l.discount
from
	lineitem l;
:n -1
