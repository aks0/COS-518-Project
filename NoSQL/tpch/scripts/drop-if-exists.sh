#!/bin/bash
v=`sudo /proj/astream561/vFabric_SQLFire_111_b42624/bin/sqlf run -file=/proj/astream561/tpcr/show-tables.sql -client-bind-address='10.1.1.2'`;

declare -a tables=('CUSTOMER' 'LINEITEM' 'NATION' 'ORDERS' \
    'PART' 'PARTSUPP' 'REGION' 'SUPPLIER');
len=${#tables[@]};
len=`expr $len - 1`;

for i in `seq 0 $len`
do
    table=${tables[$i]};
    if [[ "$v" =~ .*$table.* ]]
    then
	table=${table,,};
	sudo /proj/astream561/vFabric_SQLFire_111_b42624/bin/sqlf run -file=/proj/astream561/tpcr/drop-"$table.sql" -client-bind-address='10.1.1.2';
    fi
done
