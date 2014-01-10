arr[1]=1
arr[2]=1
arr[3]=1
arr[4]=1
arr[5]=1
arr[6]=1
arr[7]=1
arr[8]=1
arr[9]=1
arr[10]=1
arr[11]=1
arr[12]=1
arr[13]=1
arr[14]=1
arr[15]=1
arr[16]=1
arr[17]=1
arr[18]=1
arr[19]=1

for j in `seq 1 19`
do
    for i in `seq 1 ${arr[$j]}`
    do
	qgen $j | grep -v '^--' >> ./onefilequeries.sql
    done
done