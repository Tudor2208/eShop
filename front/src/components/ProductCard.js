import React from "react";
import "../css/ProductCard.css";

function ProductCard({ product }) {

    return (
        <div className="product-card">
  
        <div className="product-image">
          <img src={`${process.env.PUBLIC_URL}/images/${product.id}.png`} alt={product.title} />
        </div>
  
        <div className="product-details">
          <h2 className="product-title">{product.title}</h2>
          <p className="product-price">{product.price.toFixed(2)} Lei</p>
          <div className="product-rating">
            {Array.from({ length: 5 }, (_, i) => (
              <span key={i} className={i < product.rating ? "star filled" : "star"}>★</span>
            ))}
            <span className="reviews-count">({product.nrOfReviews})</span>
          </div>
          <p className={`product-stock ${product.stock > 0 ? "in-stock" : "out-of-stock"}`}>
                {product.stock > 0 ? `${product.stock} in stock` : "Out of Stock"}
          </p>
          <button className="add-to-cart-button" disabled={product.stock === 0}>
            {product.stock > 0 ? "Add to Cart" : "Unavailable"}
          </button>
        </div>
      </div>
    )
}

export default ProductCard