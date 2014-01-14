#!/bin/bash

declare -a arr75=(14 17 19);
declare -a arr25=();
len1=${#arr75[@]};

i=0;
for j in `seq 1 19`
do
    found=0;
    for k in `seq 0 $len1`
    do
	if [[ ${arr75[$k]} = $j ]];
	then
	    found=`expr $found + 1`;
	    continue
	fi
    done
    if [[ $found = 0 ]];
    then
	arr25[$i]=$j;
	i=`expr $i + 1`;
    fi
done
len2=${#arr25[@]};

if [[ $# != 2 ]]
then
    echo 'usage: ./script4.sh <num-queries> <output-file>';
    exit
fi

numqueries=$1
outputfile=$2

for j in `seq 1 $1`
do
    chance=`expr $RANDOM % 100`;
    if [[ $chance < 75 ]]; then
	index=`expr $RANDOM % $len1`;
	query=${arr75[$index]};
	./qgen $query | grep -v '^--' >> ./$outputfile.sql
    else
	index=`expr $RANDOM % $len2`;
	query=${arr25[$index]};
	./qgen $query | grep -v '^--' >> ./$outputfile.sql
    fi
done