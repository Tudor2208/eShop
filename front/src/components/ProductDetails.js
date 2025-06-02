import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { toast } from 'sonner';
import "../css/ProductDetails.css";

function ProductDetails() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showReviewForm, setShowReviewForm] = useState(false);
    const [newReview, setNewReview] = useState({ message: "", stars: 0 });

    const userEmail = JSON.parse(localStorage.getItem("user"))?.email;

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const productResponse = await fetch(`http://gccc1.eu-north-1.elasticbeanstalk.com:8081/api/v1/products/${id}`);
                const productData = await productResponse.json();
                setProduct(productData);

                const reviewsResponse = await fetch(`http://gccc1.eu-north-1.elasticbeanstalk.com:8081/api/v1/reviews?productId=${id}`);
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

    const handleAddReview = async () => {
        try {
            const response = await fetch(`http://gccc1.eu-north-1.elasticbeanstalk.com:8081/api/v1/reviews`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    ...newReview,
                    userEmail,
                    productId: Number(id),
                }),
            });

            if (response.ok) {
                const savedReview = await response.json();
                setReviews((prevReviews) => [...prevReviews, savedReview]);
                setNewReview({ message: "", stars: 0 });
                setShowReviewForm(false);
            } else {
                console.error("Failed to add review.");
            }
        } catch (error) {
            console.error("Error while adding review:", error);
        }
    };

    const handleDeleteReview = async (reviewId) => {
        if (window.confirm("Are you sure you want to delete this review?")) {
            try {
                const response = await fetch(`http://gccc1.eu-north-1.elasticbeanstalk.com:8081/api/v1/reviews/${reviewId}`, {
                    method: "DELETE",
                });
                if (response.ok) {
                    toast.info("Your review has been successfully deleted")
                    setReviews((prevReviews) => prevReviews.filter((review) => review.id !== reviewId));
                } else {
                    console.error("Failed to delete review.");
                }
            } catch (error) {
                console.error("Error while deleting review:", error);
            }
        }
    };

    if (loading) {
        return <div className="loading">Loading product details...</div>;
    }

    return (
        <div className="product-details-page">
            <h1>{product.title}</h1>
            <div className="product-details-content">
                <img
                    src={`https://gccc-eshop-images.s3.eu-north-1.amazonaws.com/${product.id}.png`}
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

            <div className="reviews-section">
                <h2>Customer Reviews</h2>
                {reviews.length > 0 ? (
                    <div className="reviews-list">
                        {reviews.map((review) => (
                            <div key={review.id} className="review-card">
                                <p>
                                    <strong>{review.reviewer.firstName + " " + review.reviewer.lastName}</strong>
                                    <span>({review.reviewDate})</span>
                                </p>
                                <div className="review-rating">
                                    {Array.from({ length: 5 }, (_, i) => (
                                        <span key={i} className={i < review.stars ? "star filled" : "star"}>★</span>
                                    ))}
                                </div>
                                <p>{review.message}</p>
                                {(review.reviewer.email === userEmail || JSON.parse(localStorage.getItem("user"))?.isAdmin) && (
                                <button
                                    className="delete-review-button"
                                    onClick={() => handleDeleteReview(review.id)}
                                >
                                    Delete
                                </button>
                            )}
                            </div>
                        ))}
                    </div>
                ) : (
                    <p>No reviews yet.</p>
                )}
            </div>

            {userEmail && !JSON.parse(localStorage.getItem("user"))?.isAdmin && (
            <button className="add-review-button" onClick={() => setShowReviewForm(!showReviewForm)}>
                {showReviewForm ? "Cancel" : "Add Review"}
            </button>
            )}
          
            {showReviewForm && (
                <div className="review-form">
                    <h3>Add Your Review</h3>
                    <textarea
                        className="review-textarea"
                        placeholder="Write your review here..."
                        value={newReview.message}
                        onChange={(e) => setNewReview({ ...newReview, message: e.target.value })}
                    />
                    <div className="review-rating-select">
                            <label>Rating:</label>
                            <select
                                value={newReview.stars}
                                onChange={(e) => setNewReview({ ...newReview, stars: Number(e.target.value) })}
                            >
                                {[1, 2, 3, 4, 5].map((star) => (
                                    <option key={star} value={star}>{star} Star{star > 1 && "s"}</option>
                                ))}
                            </select>
                        </div>    
                    <button className="submit-review-button" onClick={handleAddReview}>
                        Submit Review
                    </button>
                </div>
            )}
        </div>
    );
}

export default ProductDetails;
