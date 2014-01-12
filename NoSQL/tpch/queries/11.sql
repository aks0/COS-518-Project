-- @(#)11.sql	2.1.8.1
-- TPC-H/TPC-R Important Stock Identification Query (Q11)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
    ps.partkey,
    ps.supplycost,
    ps.availqty
from
	partsupp ps,
	supplier s,
	nation n
where
	ps.suppkey = s.suppkey
	and s.nationkey = n.nationkey;
:n -1
