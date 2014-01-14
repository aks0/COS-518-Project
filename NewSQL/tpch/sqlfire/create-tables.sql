CREATE TABLE NATION  ( NATIONKEY  INTEGER NOT NULL,
                            NAME       CHAR(25) NOT NULL,
                            REGIONKEY  INTEGER NOT NULL,
                            COMMENT    VARCHAR(152));

CREATE TABLE REGION  ( REGIONKEY  INTEGER NOT NULL,
                            NAME       CHAR(25) NOT NULL,
                            COMMENT    VARCHAR(152));

CREATE TABLE PART  ( PARTKEY     INTEGER NOT NULL,
                          NAME        VARCHAR(55) NOT NULL,
                          MFGR        CHAR(25) NOT NULL,
                          BRAND       CHAR(10) NOT NULL,
                          TYPE        VARCHAR(25) NOT NULL,
                          SIZE        INTEGER NOT NULL,
                          CONTAINER   CHAR(10) NOT NULL,
                          RETAILPRICE DECIMAL(15,2) NOT NULL,
                          COMMENT     VARCHAR(23) NOT NULL );

CREATE TABLE SUPPLIER ( SUPPKEY     INTEGER NOT NULL,
                             NAME        CHAR(25) NOT NULL,
                             ADDRESS     VARCHAR(40) NOT NULL,
                             NATIONKEY   INTEGER NOT NULL,
                             PHONE       CHAR(15) NOT NULL,
                             ACCTBAL     DECIMAL(15,2) NOT NULL,
                             COMMENT     VARCHAR(101) NOT NULL);

CREATE TABLE PARTSUPP ( PARTKEY     INTEGER NOT NULL,
                             SUPPKEY     INTEGER NOT NULL,
                             AVAILQTY    INTEGER NOT NULL,
                             SUPPLYCOST  DECIMAL(15,2)  NOT NULL,
                             COMMENT     VARCHAR(199) NOT NULL );

CREATE TABLE CUSTOMER ( CUSTKEY     INTEGER NOT NULL,
                             NAME        VARCHAR(25) NOT NULL,
                             ADDRESS     VARCHAR(40) NOT NULL,
                             NATIONKEY   INTEGER NOT NULL,
                             PHONE       CHAR(15) NOT NULL,
                             ACCTBAL     DECIMAL(15,2)   NOT NULL,
                             MKTSEGMENT  CHAR(10) NOT NULL,
                             COMMENT     VARCHAR(117) NOT NULL);

CREATE TABLE ORDERS  ( ORDERKEY       INTEGER NOT NULL,
                           CUSTKEY        INTEGER NOT NULL,
                           ORDERSTATUS    CHAR(1) NOT NULL,
                           TOTALPRICE     DECIMAL(15,2) NOT NULL,
                           ORDERDATE      DATE NOT NULL,
                           ORDERPRIORITY  CHAR(15) NOT NULL,  -- R
                           CLERK          CHAR(15) NOT NULL,  -- R
                           SHIPPRIORITY   INTEGER NOT NULL,
                           COMMENT        VARCHAR(79) NOT NULL);

CREATE TABLE LINEITEM ( ORDERKEY    INTEGER NOT NULL,
                             PARTKEY     INTEGER NOT NULL,
                             SUPPKEY     INTEGER NOT NULL,
                             LINENUMBER  INTEGER NOT NULL,
                             QUANTITY    DECIMAL(15,2) NOT NULL,
                             EXTENDEDPRICE  DECIMAL(15,2) NOT NULL,
                             DISCOUNT    DECIMAL(15,2) NOT NULL,
                             TAX         DECIMAL(15,2) NOT NULL,
                             RETURNFLAG  CHAR(1) NOT NULL,
                             LINESTATUS  CHAR(1) NOT NULL,
                             SHIPDATE    DATE NOT NULL,
                             COMMITDATE  DATE NOT NULL,
                             RECEIPTDATE DATE NOT NULL,
                             SHIPINSTRUCT CHAR(25) NOT NULL,  -- R
                             SHIPMODE     CHAR(10) NOT NULL,  -- R
                             COMMENT      VARCHAR(44) NOT NULL);

