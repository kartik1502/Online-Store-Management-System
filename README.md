# Online Store Management System

The Online Store Management System is a web-based application that allows store owners to manage their online store. The application is built using Spring Boot and uses JWT (JSON Web Token) authentication to secure the RESTful API endpoints.

## Features

1. [User registration and login](#user-registration-and-login)
2. [Role-based access control](#role-based-access-control)
3. [Product management](#product-management)
4. [Order management](#order-management)
5. [Shopping cart](#shopping-cart)
6. [Payment gateway integration](#payment-gateway-integration)
7. [Product search](#product-search)
8. [Order history](#order-history)

## Technical Requirements

* The backend of the application should be built using Spring Boot.
* The application should use JWT authentication to secure the RESTful API endpoints.
* The data should be stored in a relational database such as MySQL or PostgreSQL.
* The frontend of the application can be built using any modern JavaScript framework such as React, Vue.js, or Angular.
* The application should follow the RESTful API design principles and should use HTTP methods such as GET, POST, PUT, and DELETE to interact with the data.

## Installation

1. Clone the repository: `https://github.com/kartik1502/Online-Store-Management-System.git`
2. Navigate to the project directory
3. Build the project with Maven: `mvn clean install`
4. Start the application: `mvn spring-boot:run`
5. Access the application at http://localhost:8080


### User Registration and Login

Users can register and log in to the system using their email address and password.

### Role-based Access Control

The system has two types of users - store owners and customers. Store owners have access to all the features of the system while customers can only view products and place orders.

### Product Management

Store owners can add, update and delete products from their online store.

### Order Management

Store owners can view and manage orders placed by customers.

### Shopping Cart

Customers can add products to their shopping cart and place orders.

### Payment Gateway Integration

The system integrates with a payment gateway to process payments securely.

### Product Search

Customers can search for products based on keywords or categories.

### Order History

Customers can view their order history and track the status of their orders.


## About

This repository is contributed by [@kartik1502](https://github.com/kartik1502), which mainly focuses on the Spring boot application using JWT Authentication.
