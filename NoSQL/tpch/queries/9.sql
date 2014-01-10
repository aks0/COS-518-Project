-- @(#)9.sql	2.1.8.1
-- TPC-H/TPC-R Product Type Profit Measure Query (Q9)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
    n.name as nation,
    extract(year from o.orderdate) as o.year,
    sum(l.extendedprice * (1 - l.discount) - ps.supplycost * l.quantity) as amount
from
    part p,
    supplier s,
    lineitem l,
    partsupp ps,
    orders o,
    nation n
where
    s.suppkey = l.suppkey
    and ps.suppkey = l.suppkey
    and ps.partkey = l.partkey
    and p.partkey = l.partkey
    and o.orderkey = l.orderkey
    and s.nationkey = n.nationkey
    and p.name like '%:1%';
:n -1
