-- @(#)2.sql	2.1.8.2
-- TPC-H/TPC-R Minimum Cost Supplier Query (Q2)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
	s.acctbal,
	s.name,
	n.name,
	p.partkey,
	p.mfgr,
	s.address,
	s.phone,
	s.comment
from
	part p,
	supplier s,
	partsupp ps,
	nation n,
	region r
where
	p.partkey = ps.partkey
	and s.suppkey = ps.suppkey
	and p.size = :1
	and p.type like '%:2'
	and s.nationkey = n.nationkey
	and n.regionkey = r.regionkey
	and r.name = ':3'
order by
	s.acctbal desc,
	n.name,
	s.name,
	p.partkey;
:n 100
