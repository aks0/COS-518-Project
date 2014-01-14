all: create populate

create:
	sudo /proj/astream561/vFabric_SQLFire_111_b42624/bin/sqlf run -file=/proj/astream561/tpcr/create-tables.sql -client-bind-address='10.1.1.2';

populate:
	sudo /proj/astream561/vFabric_SQLFire_111_b42624/bin/sqlf run -file=/proj/astream561/tpcr/populate-tables.sql -client-bind-address='10.1.1.2'

clean:
	sudo /proj/astream561/vFabric_SQLFire_111_b42624/bin/sqlf run -file=/proj/astream561/tpcr/clean-tables.sql -client-bind-address='10.1.1.2'