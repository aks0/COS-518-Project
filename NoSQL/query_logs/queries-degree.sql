

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
	and s.suppkey = ps.suppkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.extendedprice,
	l.discount
from
	lineitem l;


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


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;



select
	l.shipmode,
	o.orderpriority
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.returnflag,
	l.linestatus,
	l.quantity,
	l.extendedprice,
	l.tax,
	l.discount
from
	lineitem l;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


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


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
	s.name
from
	supplier s,
	lineitem l1,
	orders o,
	nation n
where
	s.suppkey = l1.suppkey
	and o.orderkey = l1.orderkey;


select
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	l.orderkey,
	l.extendedprice,
	l.discount,
	o.orderdate,
	o.shippriority
from
	customer c,
	orders o,
	lineitem l
where
	c.custkey = o.custkey
	and l.orderkey = o.orderkey;


select
	p.brand,
	p.type,
	p.size,
	ps.suppkey
from
	partsupp ps,
	part p
where
	p.partkey = ps.partkey;


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


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
	s.name
from
	supplier s,
	lineitem l1,
	orders o,
	nation n
where
	s.suppkey = l1.suppkey
	and o.orderkey = l1.orderkey;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
	o.orderpriority
from
	orders o;


select
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
    o.orderdate,
    l.extendedprice,
    l.discount,
    n2.name
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
    and n1.regionkey = r.regionkey;


select
	l.extendedprice
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


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
	and s.suppkey = ps.suppkey;
