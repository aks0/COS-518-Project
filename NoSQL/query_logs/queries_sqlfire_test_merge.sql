SELECT Customers.ContactName 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.ContactTitle 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.Address 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.Region 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.PostalCode 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.ContactName 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.ContactTitle 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.Address 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.Region 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Customers.PostalCode 
FROM Customers, Orders 
WHERE Customers.CustomerID = Orders.CustomerID;

SELECT Employees.LastName 
FROM Employees, Orders 
WHERE Employees.EmployeeID = Orders.EmployeeID;

SELECT Employees.FirstName 
FROM Employees, Orders 
WHERE Employees.EmployeeID = Orders.EmployeeID;

SELECT Employees.BirthDate 
FROM Employees, Orders 
WHERE Employees.EmployeeID = Orders.EmployeeID;

SELECT Employees.HireDate 
FROM Employees, Orders 
WHERE Employees.EmployeeID = Orders.EmployeeID;

SELECT Orders.ShipName 
FROM OrderDetails, Orders 
WHERE OrderDetails.OrderID = Orders.OrderID;

SELECT Products.ProductName 
FROM OrderDetails, Products 
WHERE OrderDetails.ProductID = Products.ProductID;