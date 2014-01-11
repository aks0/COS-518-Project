

select
	l.returnflag,
	l.linestatus,
	sum(l.quantity) as sum_qty,
	sum(l.extendedprice) as sum_base_price,
	sum(l.extendedprice * (1 - l.discount)) as sum_disc_price,
	sum(l.extendedprice * (1 - l.discount) * (1 + l.tax)) as sum_charge,
	avg(l.quantity) as avg_qty,
	avg(l.extendedprice) as avg_price,
	avg(l.discount) as avg_disc,
	count(*) as count_order
from
	lineitem l
where
	l.orderkey <= 84
group by
	l.returnflag,
	l.linestatus
order by
	l.returnflag,
	l.linestatus;


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
	and p.size = 28
	and p.type like '%COPPER'
	and s.nationkey = n.nationkey
	and n.regionkey = r.regionkey
	and r.name = 'ASIA'
order by
	s.acctbal desc,
	n.name,
	s.name,
	p.partkey;


select
	l.orderkey,
	sum(l.extendedprice * (1 - l.discount)) as revenue,
	o.orderdate,
	o.shippriority
from
	customer c,
	orders o,
	lineitem l
where
	c.mktsegment = 'AUTOMOBILE'
	and c.custkey = o.custkey
	and l.orderkey = o.orderkey
	and o.orderdate < '1995-03-17'
	and l.shipdate > '1995-03-17'
group by
	l.orderkey,
	o.orderdate,
	o.shippriority
order by
	revenue desc,
	o.orderdate;


select
	o.orderpriority,
	count(*) as order_count
from
	orders o
where
	o.orderdate >= '1995-11-01'
group by
	o.orderpriority
order by
	o.orderpriority;


select
	n.name,
	sum(l.extendedprice * (1 - l.discount)) as revenue
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
	and n.regionkey = r.regionkey
	and r.name = 'AFRICA'
	and o.orderdate >= '1993-01-01'
group by
	n.name
order by
	revenue desc;


select
	sum(l.extendedprice * l.discount) as revenue
from
	lineitem l
where
	l.shipdate >= '1994-01-01'
	and l.discount between 0.04 - 0.01 and 0.04 + 0.01
	and l.quantity < 24;


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
        (n1.name = 'IRAN' and n2.name = 'MOZAMBIQUE')
        or (n1.name = 'MOZAMBIQUE' and n2.name = 'IRAN')
    );


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
    and n1.regionkey = r.regionkey
    and r.name = 'AMERICA'
    and s.nationkey = n2.nationkey
    and p.type = 'SMALL BRUSHED COPPER';


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
    and s.nationkey = n.nationkey
    and p.name like '%sky%';


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
	and o.orderdate >= '1994-02-01'
	and l.returnflag = 'R'
	and c.nationkey = n.nationkey;


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
	and s.nationkey = n.nationkey
	and n.name = 'IRAQ';



select
	l.shipmode,
	sum(case
		when o.orderpriority = '1-URGENT'
			or o.orderpriority = '2-HIGH'
			then 1
		else 0
	end) as high_line_count,
	sum(case
		when o.orderpriority <> '1-URGENT'
			and o.orderpriority <> '2-HIGH'
			then 1
		else 0
	end) as low_line_count
from
	orders o,
	lineitem l
where
	o.orderkey = l.orderkey
	and l.shipmode in ('RAIL', 'FOB')
	and l.commitdate < l.receiptdate
	and l.shipdate < l.commitdate
	and l.receiptdate >= '1994-01-01'
group by
	l.shipmode
order by
	l.shipmode;


select
	s.name,
    count(*)
from
	supplier s,
	lineitem l1,
	orders o,
	nation n
where
	s.suppkey = l1.suppkey
	and o.orderkey = l1.orderkey
	and o.orderstatus = 'F'
	and l1.receiptdate > l1.commitdate
	and s.nationkey = n.nationkey
	and n.name = 'pending'
group by
	s.name
order by
	s.name;


select
	sum(case
		when p.type like 'PROMO%'
			then l.extendedprice * (1 - l.discount)
		else 0
	end) as promo_revenue,
	sum(l.extendedprice * (1 - l.discount))
from
	lineitem l,
	part p
where
	l.partkey = p.partkey
	and l.shipdate >= '1995-08-01'


select
	s.name,
	s.address
from
	supplier s,
	nation n
where
    s.nationkey = n.nationkey
	and n.name = ''
order by
	s.name;


select
	p.brand,
	p.type,
	p.size,
	count(distinct ps.suppkey) as supplier_cnt
from
	partsupp ps,
	part p
where
	p.partkey = ps.partkey
	and p.brand <> 'Brand#15'
	and p.type not like 'SMALL BURNISHED%'
	and p.size in (32, 7, 13, 42, 31, 44, 8, 22)
group by
	p.brand,
	p.type,
	p.size
order by
	supplier_cnt desc,
	p.brand,
	p.type,
	p.size;


select
	sum(l.extendedprice) as avg_yearly
from
	lineitem l,
	part p
where
	p.partkey = l.partkey
	and p.brand = 'Brand#44'
	and p.container = 'WRAP CAN';


select
	c.name,
	c.custkey,
	o.orderkey,
	o.orderdate,
	o.totalprice,
	sum(l.quantity)
from
	customer c,
	orders o,
	lineitem l
where
    c.custkey = o.custkey
	and o.orderkey = l.orderkey
group by
	c.name,
	c.custkey,
	o.orderkey,
	o.orderdate,
	o.totalprice
order by
	o.totalprice desc,
	o.orderdate;


select
	l.extendedprice,
    l.discount
from
	lineitem l,
	part p
where
	(
		p.partkey = l.partkey
		and p.brand = 'Brand#23'
		and p.container in ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')
		and l.quantity >= 7 and l.quantity <= 7 + 10
		and p.size between 1 and 5
		and l.shipmode in ('AIR', 'AIR REG')
		and l.shipinstruct = 'DELIVER IN PERSON'
	)
	or
	(
		p.partkey = l.partkey
		and p.brand = 'Brand#31'
		and p.container in ('MED BAG', 'MED BOX', 'MED PKG', 'MED PACK')
		and l.quantity >= 19 and l.quantity <= 19 + 10
		and p.size between 1 and 10
		and l.shipmode in ('AIR', 'AIR REG')
		and l.shipinstruct = 'DELIVER IN PERSON'
	)
	or
	(
		p.partkey = l.partkey
		and p.brand = 'Brand#12'
		and p.container in ('LG CASE', 'LG BOX', 'LG PACK', 'LG PKG')
		and l.quantity >= 30 and l.quantity <= 30 + 10
		and p.size between 1 and 15
		and l.shipmode in ('AIR', 'AIR REG')
		and l.shipinstruct = 'DELIVER IN PERSON'
	);
