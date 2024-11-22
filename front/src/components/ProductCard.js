import React, { useState } from "react";
import { toast } from 'sonner';
import "../css/ProductCard.css";

function ProductCard({ product, onClick }) {
  const [showDialog, setShowDialog] = useState(false);
  const [quantity, setQuantity] = useState(1);
  const user = JSON.parse(localStorage.getItem("user"));
  const userEmail = user ? user.email : null;

  const handleAddToCartClick = () => {
      if (product.stock > 0) {
          setShowDialog(true); 
      }
  };

  const handleQuantityChange = (e) => {
      setQuantity(e.target.value);
  };

  const handleSubmit = async (event) => {
      event.preventDefault(); 

      if (quantity <= 0 || quantity > product.stock) {
          toast.error("Please enter a valid quantity between 1 and the available stock.");
          return;
      }

      const payload = {
        items: [
          {
              productId: product.id,
              quantity: parseInt(quantity),
          }
        ]
      };

      try {
        const response = await fetch(`http://localhost:8082/api/v1/carts/${userEmail}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(payload),
        });

        if (response.ok) {
            const cartData = await response.json(); 
            setShowDialog(false);
            localStorage.setItem("cart", JSON.stringify(cartData.items)); 
            window.location.reload();
            toast.success("Product added to cart!");
        } else {
            const data = await response.json();
            toast.error(data.message || "Something went wrong.");
        }
    } catch (error) {
        toast.error("An error occurred while adding the product to the cart.");
    }
};

  const handleCloseDialog = () => {
      setShowDialog(false); 
      setQuantity(1); 
  };

  return (
      <div className="product-card">
          <div className="product-image">
              <img
                  src={`${process.env.PUBLIC_URL}/images/${product.id}.png`}
                  alt={product.title}
              />
          </div>

          <div className="product-details">
              <h2 className="product-title" onClick={onClick}>{product.title}</h2>
              <p className="product-price">{product.price.toFixed(2)} Lei</p>
              <div className="product-rating">
                  {Array.from({ length: 5 }, (_, i) => (
                      <span
                          key={i}
                          className={i < product.rating ? "star filled" : "star"}
                      >
                          ★
                      </span>
                  ))}
                  <span className="reviews-count">({product.nrOfReviews})</span>
              </div>
              <p
                  className={`product-stock ${
                      product.stock > 0 ? "in-stock" : "out-of-stock"
                  }`}
              >
                  {product.stock > 0 ? `${product.stock} in stock` : "Out of Stock"}
              </p>
              <button
                  className="add-to-cart-button"
                  disabled={product.stock === 0}
                  onClick={(event) => handleAddToCartClick(event)} 
              >
                  {product.stock > 0 ? "Add to Cart" : "Unavailable"}
              </button>
          </div>

          {showDialog && (
              <div className="quantity-dialog">
                  <div className="dialog-content">
                      <h3>Enter Quantity</h3>
                      <form onSubmit={handleSubmit}>
                          <input
                              type="number"
                              min="1"
                              max={product.stock}
                              value={quantity}
                              onChange={handleQuantityChange}
                              autoFocus
                          />
                          <div className="dialog-actions">
                              <button type="submit" onClick={(event) => event.stopPropagation()}>Add to Cart</button>
                              <button
                                  type="button"
                                  onClick={handleCloseDialog}
                              >
                                  Cancel
                              </button>
                          </div>
                      </form>
                  </div>
              </div>
          )}
      </div>
  );
}

export default ProductCard;
