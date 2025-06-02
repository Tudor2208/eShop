# Key Features
- Login / Register functionality
- Customers can view and edit their profiles, including phone number and email address
- Customers can search for products by name
- Customers can filter products by category and sort them by price, stock, or posting date
- Customers can add products to their cart
- Customers can place an order based on the items in their cart
- Admins can add new products, modify the stock of existing ones, or delete products
- Admins can add new categories or delete existing ones

# Architecture Overview

The system consists of three main modules: User-Manager, Product-Manager, Order-Manager, and a React-based Front-End. Each microservice is responsible for a specific set of functionalities, enabling clear separation of concerns and better scalability.

## User-Manager:

- Responsible for managing users, including registration and authentication.
- Uses a PostgreSQL database with a single table (users) to store user information, such as identification data and roles.
- Publishes events on two Kafka topics:
  - user.create: Notifies about the creation of a new user.
  - user.delete: Signals the deletion of a user.
    
## Product-Manager:

- Manages the products in the store, including handling categories and reviews.
- Uses an Oracle database structured with four tables:
  - products: Stores product details.
  - users: Stores user IDs.
  - reviews: Stores reviews associated with products.
  - categories: Stores product categories.
- Consumes events from Kafka topics (user.create and user.delete) to synchronize user data.
- Publishes events on Kafka topics (product.create and product.delete) to notify product additions or deletions.
  
## Order-Manager:

- Manages customer orders and shopping carts.
- Allows adding/removing products from the shopping cart.
- Facilitates creating orders based on cart items and updates product stock after order placement.
- Uses a Microsoft SQL database to store shopping cart and order data.
- Consumes events from Kafka topics (user.create, user.delete, product.create, product.delete) to synchronize users and products within its database.
- Each microservice interacts with Kafka for event-driven communication, ensuring synchronization between users, products, and orders across the system.

![diagrama_conceptuala_tema_2](https://github.com/user-attachments/assets/b76ad6db-8782-48b9-be06-2fa9007fdd38)

# App Views

![image](https://github.com/user-attachments/assets/5a55bf1c-9089-42b2-b384-c2e1c204547d)
![image](https://github.com/user-attachments/assets/7a63d80a-64f1-424f-8a82-a2ac99384ef7)
![image](https://github.com/user-attachments/assets/71c117da-46e4-4113-a9e0-767be99a7a79)
![image](https://github.com/user-attachments/assets/834ae36a-2bf0-4ede-93b8-ef95d6834979)
![image](https://github.com/user-attachments/assets/9190c8f4-73d0-45ab-9c2a-df20eea2c000)





