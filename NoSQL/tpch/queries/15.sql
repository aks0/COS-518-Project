-- @(#)20.sql	2.1.8.1
-- TPC-H/TPC-R Potential Part Promotion Query (Q20)
-- Function Query Definition
-- Approved February 1998
:x
:o
select
	s.name,
	s.address
from
	supplier s,
	nation n
where
    s.nationkey = n.nationkey
	and n.name = ':3'
order by
	s.name;
:n -1
