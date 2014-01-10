-- @(#)8.sql	2.1.8.1
-- TPC-H/TPC-R National Market Share Query (Q8)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
    extract(year from o.orderdate) as o.year,
    sum(l.extendedprice * (1 - l.discount)) as volume,
    n2.name as nation
from
    part p,
    supplier s,
    lineitem l,
    orders o,
    customer c,
    nation n1,
    nation n2,
    region r
where
    p.partkey = l.partkey
    and s.suppkey = l.suppkey
    and l.orderkey = o.orderkey
    and o.custkey = c.custkey
    and c.nationkey = n1.nationkey
    and n1.regionkey = r.regionkey
    and r.name = ':2'
    and s.nationkey = n2.nationkey
    and p.type = ':3';
:n -1
