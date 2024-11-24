import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ProductCard from "./ProductCard";
import "../css/Products.css";

function Products({ categoryId, searchText }) {
    const [sortOption, setSortOption] = useState("price");
    const [sortOrder, setSortOrder] = useState("asc");

    const [currentPage, setCurrentPage] = useState(0);
    const [products, setProducts] = useState([]);
    const [totalPages, setTotalPages] = useState(1);

    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem("user"));

    const fetchProducts = async (page = 0, sortBy, sortOrder) => {
        try {
            let url = `http://localhost:8081/api/v1/products?page=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}`;
            if (categoryId !== null) {
                url += `&categoryId=${categoryId}`;
            }
            
            if (searchText !== null && searchText.trim() !== '') {
                url += `&keywords=${encodeURIComponent(searchText)}`;
            }
            
            const response = await fetch(url);
            const data = await response.json();
    
            setProducts(data.content);
            setTotalPages(data.totalPages);
        } catch (error) {
            console.error("Failed to fetch products", error);
        }
    };

    useEffect(() => {
        fetchProducts(currentPage, sortOption, sortOrder);
    }, [currentPage, categoryId, searchText, sortOption, sortOrder]);

    const handleSortOptionChange = (e) => {
        setSortOption(e.target.value);
        setCurrentPage(0); 
    };

    const handleSortOrderChange = (e) => {
        setSortOrder(e.target.value);
        setCurrentPage(0); 
    };

    const handleNextPage = () => {
        if (currentPage + 1 < totalPages) {
            setCurrentPage(prevPage => prevPage + 1);
        }
    };

    const handlePreviousPage = () => {
        if (currentPage > 0) {
            setCurrentPage(prevPage => prevPage - 1);
        }
    };

    const handleCardClick = (id) => {
        navigate(`/product/${id}`);
    };

    const redirectToCreateProductPage = () => {
        navigate("/create-product");
    };

    const redirectToCategoryManagementPage = () => {
        navigate("/categories");
    };

    return (
        <>
            <div className="sort-container">
                <label htmlFor="sortBy" className="sort-label">Sort by:</label>
                <select
                    id="sortBy"
                    value={sortOption}
                    onChange={handleSortOptionChange}
                    className="sort-dropdown"
                >
                    <option value="price">Price</option>
                    <option value="postDate">Post date</option>
                    <option value="stock">Stock</option>
                </select>

                <label htmlFor="sortOrder" className="sort-label">Order:</label>
                <select
                    id="sortOrder"
                    value={sortOrder}
                    onChange={handleSortOrderChange}
                    className="sort-dropdown"
                >
                    <option value="asc">Ascending</option>
                    <option value="desc">Descending</option>
                </select>

                {user?.isAdmin && (
                    <button
                        className="add-product-button"
                        onClick={redirectToCreateProductPage}
                    >
                        Add Product
                    </button>
                )}

                {user?.isAdmin && (
                    <button
                        className="update-categories-button"
                        onClick={redirectToCategoryManagementPage}
                    >
                        Update Categories
                    </button>
                )}
            </div>

            <div className="product-grid">
                {products.map((product, index) => (
                    <ProductCard key={index} product={product} onClick={() => handleCardClick(product.id)} />
                ))}
            </div>

            <div className="pagination-buttons">
                <button 
                    onClick={handlePreviousPage} 
                    disabled={currentPage === 0}
                    className="pagination-button"
                >
                    Previous
                </button>
                <button 
                    onClick={handleNextPage} 
                    disabled={currentPage === totalPages - 1}
                    className="pagination-button"
                >
                    Next
                </button>
            </div>
        </>
    );
}

export default Products;
