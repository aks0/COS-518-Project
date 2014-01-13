#!/bin/bash

if [[ $# != 3 ]]
then
    echo 'usage: ./script1.sh <num-template> <num-queries> <output-file>';
    exit
fi

templates=$1
numqueries=$2
outputfile=$3

for j in `seq 1 $1`
do
    query=`expr $RANDOM % $templates`;
    query=`expr $query + 1`;
    ./qgen $query | grep -v '^--' >> ./$outputfile.sql
done