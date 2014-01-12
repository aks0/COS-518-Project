#!/bin/bash
declare -a tables=('CUSTOMER' 'LINEITEM' 'NATION' 'ORDERS' \
    'PART' 'PARTSUPP' 'REGION' 'SUPPLIER');
len=${#tables[@]};
len=`expr $len - 1`;

for i in `seq 0 $len`
do
    table=${tables[$i]};
    table=${table,,};
    filename="/proj/astream561/tpcr/drop-$table.sql"
    echo "drop table $table;" > $filename
done