import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import '../css/ShoppingCartPage.css';

function ShoppingCartPage() {
  const [cartItems, setCartItems] = useState([]);
  const [productDetails, setProductDetails] = useState([]);
  const user = JSON.parse(localStorage.getItem("user"));
  const navigate = useNavigate();

  useEffect(() => {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    setCartItems(cart);
    fetchProductDetails(cart);
  }, []);

  const fetchProductDetails = async (cart) => {
    const productPromises = cart.map(async (item) => {
      try {
        const response = await fetch(`http://gccc1.eu-north-1.elasticbeanstalk.com:8081/api/v1/products/${item.productId}`);
        if (response.ok) {
          const productData = await response.json();
          return {
            productId: item.productId,
            title: productData.title,
            price: productData.price,
            image: `${process.env.PUBLIC_URL}/images/${productData.id}.png`,
          };
        } else {
          return null;
        }
      } catch (error) {
        return null;
      }
    });

    const products = await Promise.all(productPromises);
    setProductDetails(products.filter((product) => product !== null));
  };

  const handleProductClick = (productId) => {
    navigate(`/product/${productId}`);
  };

  const handleDeleteItem = async (productId) => {
    const payload = {
      items: [
        {
            productId: productId,
            quantity: 0, 
        }
      ]
    };

    try {
        const response = await fetch(`http://gccc2.eu-north-1.elasticbeanstalk.com:8082/api/v1/carts/${user.email}`, {
            method: "PUT",  
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),  
        });

        if (response.ok) {
            const cartData = await response.json();  
            localStorage.setItem("cart", JSON.stringify(cartData.items));  
            setCartItems(cartData.items);  
            toast.success("Product removed from cart!");  
        } else {
            const data = await response.json();
            toast.error(data.message || "Something went wrong.");
        }
    } catch (error) {
        toast.error("An error occurred while removing the product from the cart.");
    }
  };

  const calculateTotalPrice = () => {
    return cartItems.reduce((total, item) => {
      const product = productDetails.find((prod) => prod.productId === item.productId);
      return product ? total + item.quantity * product.price : total;
    }, 0);
  };

  const handlePlaceOrder = async () => {

    if (cartItems.length === 0) {
      toast.error("Your cart is empty. Add items to your cart before placing an order.");
      return;
    }

    const orderItems = cartItems.map((item) => ({
      productId: item.productId,
      quantity: item.quantity,
    }));

    const orderData = {
      userEmail: user.email,
      orderItems: orderItems,
    };

    try {

      const response = await fetch('http://gccc2.eu-north-1.elasticbeanstalk.com:8082/api/v1/orders', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(orderData),
      });

      if (response.ok) {
        const orderResponse = await response.json();
        toast.success("Order placed successfully!");
        for (const item of cartItems) {
          await handleDeleteItem(item.productId); 
        }

      } else {
        const data = await response.json();
        toast.error(data.message || "Failed to place order.");
      }
    } catch (error) {
      toast.error("An error occurred while placing the order.");
    }
  };

  return (
    <div className="shopping-cart-page">
      <h1>Your Shopping Cart ({cartItems.length} items)</h1> 

      {cartItems.length === 0 ? (
        <p>Your cart is empty. Start shopping!</p>
      ) : (
        <div className="cart-items">
          {cartItems.map((item) => {
            const product = productDetails.find((prod) => prod.productId === item.productId);

            return product ? (
              <div
                key={item.productId}
                className="cart-item"
                onClick={() => handleProductClick(item.productId)}
              >
                <img src={product.image} alt={product.title} className="cart-item-image" />
                <div className="cart-item-details">
                  <p><b>{product.title}</b></p>
                  <p>Quantity: {item.quantity}</p>
                  <p>Price: {product.price} Lei</p>
                </div>
                <button
                  className="delete-button"
                  onClick={(e) => {
                    e.stopPropagation();
                    handleDeleteItem(item.productId); 
                  }}
                >
                  Delete
                </button>
              </div>
            ) : (
              <p key={item.productId}>Loading product details...</p>
            );
          })}
        </div>
      )}

      <div className="total-price">
        <h3>Total: {calculateTotalPrice().toFixed(2)} Lei</h3>
      </div>

      {cartItems.length > 0 &&  
      <div className="place-order">
        <button onClick={handlePlaceOrder} className="place-order-button">
          Place Order
        </button>
      </div>}
     
    </div>
  );
}

export default ShoppingCartPage;
