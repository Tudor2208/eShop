import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import '../css/OrdersPage.css';

function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const user = JSON.parse(localStorage.getItem('user'));
  const navigate = useNavigate();

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await fetch(`http://localhost:8082/api/v1/orders?userId=${user.email}`);
      if (response.ok) {
        const data = await response.json();
        setOrders(data);
      } else {
        toast.error('Failed to fetch orders.');
      }
    } catch (error) {
      toast.error('An error occurred while fetching orders.');
    }
  };

  return (
    <div className="orders-page">
      <h1>Your Orders</h1>

      {orders.length === 0 ? (
        <p>You have no orders yet.</p>
      ) : (
        <div className="orders-list">
          {orders.map((order, key) => (
            <div key={key} className="order-item">
              <h3>Order #{key+1}</h3>
              <p><strong>Date:</strong> {new Date(order.orderDate).toLocaleDateString()}</p>
              <p><strong>Total:</strong> {order.price.toFixed(2)} Lei</p>
              <p><strong>Items:</strong></p>
              <ol>
                {order.items.map((item) => (
                  <li key={item.productId}>
                    {item.productName} x {item.quantity}: {item.productPrice} Lei
                  </li>
                ))}
              </ol>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default OrdersPage;
