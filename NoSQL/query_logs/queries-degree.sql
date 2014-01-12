

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


select
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


select
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


select
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


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
	o.orderpriority
from
	orders o;


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
	o.orderpriority
from
	orders o;


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
	l.shipmode,
	o.orderpriority
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey;


select
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


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
	l.returnflag,
	l.linestatus,
	l.quantity,
	l.extendedprice,
	l.tax,
	l.discount
from
	lineitem l;


select
	o.orderpriority
from
	orders o;


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
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


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
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


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
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


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
    and c.nationkey = n2.nationkey;


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
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


select
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


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
	lineitem l;


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
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;


select
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


select
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;



select
	l.shipmode,
	o.orderpriority
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey;



select
	l.shipmode,
	o.orderpriority
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey;


select
    n.name,
    o.orderdate,
    l.extendedprice,
    l.discount,
    ps.supplycost,
    l.quantity
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
    and s.nationkey = n.nationkey;


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
	l.shipmode,
	o.orderpriority
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey;


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
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	p.partkey = l.partkey;
