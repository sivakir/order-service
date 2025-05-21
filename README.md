# Getting Started

### Getting Code
* Clone the repository with the URL https://github.com/sivakir/order-service.git
* Do a maven clean, install and run the OrderServiceApplication from IDE
* After the application startup visit this URL for OpenAPI documentation http://localhost:8080/swagger-ui/index.html

### Reference Documentation
For further reference, please consider the following sections:

* As per the requirement it has two users which are pre-cnfigured
* First one is "user" with "USER" role and password is same as "password"
* Second one is "admin" with "ADMIN" role and password is same as "password"
* Creating order is configured for only "USER" role
* Updating and deleting order is configured only for "ADMIN" role
* GetOrderByID and GetAllOrders is configured for "USER" and "ADMIN" roles
