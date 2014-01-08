arr[1]=10
arr[2]=10
arr[3]=10
arr[4]=10
arr[5]=10
arr[6]=10
arr[7]=10
arr[8]=10
arr[9]=10
arr[10]=10
arr[11]=10
arr[12]=10
arr[13]=10
arr[14]=10
arr[15]=10
arr[16]=10
arr[17]=10
arr[18]=10
arr[19]=10
arr[20]=10

outputdir=$1
if [ ! -d "$outputdir" ]; then
    mkdir $outputdir;
fi

for j in `seq 1 20`
do
    for i in `seq 1 ${arr[$j]}`
    do
	qgen $j | grep -v '^--' >> ./$outputdir/$j.sql
    done
done