#!/bin/bash

if [[ $# != 2 ]]
then
    echo 'usage: ./script1.sh <num-queries> <output-file>';
    exit
fi

numqueries=$1
outputfile=$2

for j in `seq 1 $1`
do
    query=`expr $RANDOM % 19`;
    query=`expr $query + 1`;
    ./qgen $query | grep -v '^--' >> ./$outputfile.sql
done