COS-518-Project
===============

In this project, we present a method for configuring the placement of data onto
servers in NewSQL systems to minimize the cost of executing a known query
workload.  Mainly, we apply ideas from database literature on optimal view
materialization to colocate related data necessary for executing important
joins within the memory of each data server. Through experiments in Emulab
using SQLFire, we show that using our method to determine data placement
configurations allows for adaptivity and improved performance with query
workloads when compared to other data placement strategies.