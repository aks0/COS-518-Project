-- @(#)7.sql	2.1.8.1
-- TPC-H/TPC-R Volume Shipping Query (Q7)
-- Functional Query Definition
-- Approved February 1998
:x
:o
select
    n1.name,
    n2.name,
    l.shipdate,
    l.extendedprice,
    l.discount
from
    supplier s,
    lineitem l ,
    orders o,
    customer c,
    nation n1,
    nation n2
where
    s.suppkey = l.suppkey
    and o.orderkey = l.orderkey
    and c.custkey = o.custkey
    and s.nationkey = n1.nationkey
    and c.nationkey = n2.nationkey
    and (
        (n1.name = ':1' and n2.name = ':2')
        or (n1.name = ':2' and n2.name = ':1')
    );
:n -1
