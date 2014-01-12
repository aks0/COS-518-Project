-- @(#)10.sql	2.1.8.1
-- TPC-H/TPC-R Returned Item Reporting Query (Q10)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
    c.custkey,
    c.name,
    l.extendedprice,
    l.discount,
    c.acctbal,
    n.name,
    c.address,
    c.phone,
    c.comment
from
	customer c,
	orders o,
	lineitem l,
	nation n
where
	c.custkey = o.custkey
	and l.orderkey = o.orderkey
	and c.nationkey = n.nationkey;
:n 20
