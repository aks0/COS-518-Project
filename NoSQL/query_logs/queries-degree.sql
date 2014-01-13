

select
	s.name,
	s.address
from
	supplier s,
	nation n
where
    s.nationkey = n.nationkey;


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
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


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
	l.shipmode,
	o.orderpriority
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey;


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
	c.name,
	c.custkey,
	o.orderkey,
	o.orderdate,
	o.totalprice,
	l.quantity
from
	customer c,
	orders o,
	lineitem l
where
	c.custkey = o.custkey
	and o.orderkey = l.orderkey;


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
	p.type,
	l.extendedprice,
	l.discount
from
	lineitem l,
	part p
where
	l.partkey = p.partkey;


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
