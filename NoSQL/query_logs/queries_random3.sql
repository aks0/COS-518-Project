


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
	o.orderpriority
from
	orders o;


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
	n.name,
	l.extendedprice,
	l.discount
from
	customer c,
	orders o,
	lineitem l,
	supplier s,
	nation n,
	region r
where
	c.custkey = o.custkey
	and l.orderkey = o.orderkey
	and l.suppkey = s.suppkey
	and c.nationkey = s.nationkey
	and s.nationkey = n.nationkey
	and n.regionkey = r.regionkey;


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
	s.name,
	s.address
from
	supplier s,
	nation n
where
    s.nationkey = n.nationkey;


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
	l.extendedprice,
	l.discount
from
	lineitem l;


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
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


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
	s.name,
	s.address
from
	supplier s,
	nation n
where
    s.nationkey = n.nationkey;



select
	l.shipmode,
	o.orderpriority
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey;


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
	lineitem l;


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
	s.name,
	s.address
from
	supplier s,
	nation n
where
    s.nationkey = n.nationkey;


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
	l.returnflag,
	l.linestatus,
	l.quantity,
	l.extendedprice,
	l.tax,
	l.discount
from
	lineitem l;


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
	l.extendedprice,
	l.discount
from
	lineitem l;


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
	o.orderpriority
from
	orders o;
