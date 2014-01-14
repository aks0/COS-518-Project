

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
	s.name,
	s.address
from
	supplier s,
	nation n
where
    s.nationkey = n.nationkey;


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
