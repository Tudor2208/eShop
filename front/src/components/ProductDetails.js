import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "../css/ProductDetails.css";

function ProductDetails() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);

    // Fetch product details and reviews
    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const productResponse = await fetch(`http://localhost:8081/api/v1/products/${id}`);
                const productData = await productResponse.json();
                setProduct(productData);

                // Fetch reviews for this product
                const reviewsResponse = await fetch(`http://localhost:8081/api/v1/reviews?productId=${id}`);
                const reviewsData = await reviewsResponse.json();
                setReviews(reviewsData.content);

                setLoading(false);
            } catch (error) {
                console.error("Failed to fetch product details and reviews", error);
                setLoading(false);
            }
        };

        fetchProduct();
    }, [id]);

    if (loading) {
        return <div>Loading product details...</div>;
    }

    return (
        <div className="product-details-page">
            <h1>{product.title}</h1>
            <div className="product-details-content">
                <img 
                    src={`${process.env.PUBLIC_URL}/images/${product.id}.png`} 
                    alt={product.title} 
                    className="product-details-image"
                />
                <div className="product-details-info">
                    <p><strong>Price:</strong> {product.price.toFixed(2)} Lei</p>
                    <p><strong>Category:</strong> {product.category?.name || "Uncategorized"}</p>
                    <p><strong>Stock:</strong> {product.stock > 0 ? `${product.stock} in stock` : "Out of Stock"}</p>
                    <p><strong>Rating:</strong> {product.rating.toFixed(1)} ({product.nrOfReviews} reviews)</p>
                    <p><strong>Post Date:</strong> {product.postDate}</p>
                </div>
            </div>

            {/* Specifications Table */}
            <table className="specifications-table">
                <thead>
                    <tr>
                        <th>Specification</th>
                        <th>Value</th>
                    </tr>
                </thead>
                <tbody>
                    {Object.entries(product.specifications || {}).map(([key, value], index) => (
                        <tr key={index}>
                            <td>{key}</td>
                            <td>{value}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Reviews Section */}
            <div className="reviews-section">
                <h2>Customer Reviews</h2>
                {reviews.length > 0 ? (
                    <div className="reviews-list">
                        {reviews.map((review) => (
                            <div key={review.id} className="review-card">
                                <p><strong>{review.reviewer.firstName + " " + review.reviewer.lastName}</strong> <span>({review.reviewDate})</span></p>
                                <div className="review-rating">
                                    {Array.from({ length: 5 }, (_, i) => (
                                        <span key={i} className={i < review.stars ? "star filled" : "star"}>★</span>
                                    ))}
                                </div>
                                <p>{review.message}</p>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p>No reviews yet.</p>
                )}
            </div>
        </div>
    );
}

export default ProductDetails;
