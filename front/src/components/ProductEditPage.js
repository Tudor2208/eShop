import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from 'sonner';
import '../css/ProductEditPage.css';

function ProductEditPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [title, setTitle] = useState('');
  const [price, setPrice] = useState('');
  const [stock, setStock] = useState('');
  const [category, setCategory] = useState('');
  const [specifications, setSpecifications] = useState({}); 
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchProduct = async () => {
      const response = await fetch(`http://localhost:8081/api/v1/products/${id}`);
      if (response.ok) {
        const productData = await response.json();
        setProduct(productData);
        setTitle(productData.title);
        setPrice(productData.price);
        setStock(productData.stock);
        setCategory(productData.category.id);
        setSpecifications(productData.specifications || {});
      } else {
        toast.error("Failed to fetch product details.");
      }
    };

    const fetchCategories = async () => {
      const response = await fetch('http://localhost:8081/api/v1/categories');
      if (response.ok) {
        const categoriesData = await response.json();
        setCategories(categoriesData.content);
      } else {
        toast.error("Failed to fetch categories.");
      }
    };

    fetchProduct();
    fetchCategories();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const updatedProduct = {
      title,
      price: parseFloat(price),
      stock: parseInt(stock),
      categoryId: category,
      specifications
    };

    try {
      const response = await fetch(`http://localhost:8081/api/v1/products/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedProduct),
      });

      if (response.ok) {
        toast.success("Product updated successfully!");
        navigate(`/product/${id}`); 
      } else {
        const data = await response.json();
        toast.error(data.message || "Failed to update the product.");
      }
    } catch (error) {
      toast.error("An error occurred while updating the product.");
    }
  };

  if (!product) return <div>Loading...</div>;

  return (
    <div className="edit-product-page">
      <h2>Edit Product</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="edit-product-title">Title</label>
          <input
            id="edit-product-title"
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>

        <div>
          <label htmlFor="edit-product-price">Price</label>
          <input
            id="edit-product-price"
            type="number"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
          />
        </div>

        <div>
          <label htmlFor="edit-product-stock">Stock</label>
          <input
            id="edit-product-stock"
            type="number"
            value={stock}
            onChange={(e) => setStock(e.target.value)}
          />
        </div>

        <div>
          <label htmlFor="edit-product-category">Category</label>
          <select
            id="edit-product-category"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
          >
            <option value="">Select Category</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.name}
              </option>
            ))}
          </select>
        </div>

        <div className="edit-product-specifications-section">
          <label htmlFor="edit-product-specifications">Specifications</label>
          <div className="edit-product-specifications-inputs">
            {Object.keys(specifications).map((key, index) => (
              <div key={index}>
                <input
                  id={`edit-product-specification-name-${index}`}
                  type="text"
                  value={key}
                  onChange={(e) => {
                    const updatedSpecs = { ...specifications };
                    delete updatedSpecs[key];
                    updatedSpecs[e.target.value] = specifications[key];
                    setSpecifications(updatedSpecs);
                  }}
                  placeholder="Specification Name"
                />
                <input
                  id={`edit-product-specification-value-${index}`}
                  type="text"
                  value={specifications[key]}
                  onChange={(e) => {
                    setSpecifications({ ...specifications, [key]: e.target.value });
                  }}
                  placeholder="Specification Value"
                />
      
                <button
                  type="button"
                  id="del-btn"
                  className="edit-product-specification-delete-button"
                  onClick={() => {
                    const updatedSpecs = { ...specifications };
                    delete updatedSpecs[key];
                    setSpecifications(updatedSpecs);
                  }}
                >
                  Delete
                </button>
              </div>
            ))}
          </div>
          <button
            type="button"
            className="edit-product-add-specification-button"
            onClick={() => setSpecifications({ ...specifications, 'New Spec': '' })}
          >
            Add New Specification
          </button>
        </div>

        <button type="submit">Save Changes</button>
      </form>
    </div>
  );
}

export default ProductEditPage;
