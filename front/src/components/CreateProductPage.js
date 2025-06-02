import React, { useState, useEffect } from "react";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import "../css/ProductEditPage.css";

function CreateProductPage() {
  const navigate = useNavigate();
  const [title, setTitle] = useState("");
  const [price, setPrice] = useState("");
  const [stock, setStock] = useState("");
  const [category, setCategory] = useState("");
  const [specifications, setSpecifications] = useState({}); 
  const [categories, setCategories] = useState([]);
  const [imageFile, setImageFile] = useState(null);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch("http://gccc1.eu-north-1.elasticbeanstalk.com:8081/api/v1/categories");
        if (response.ok) {
          const categoriesData = await response.json();
          setCategories(categoriesData.content);
        } else {
          toast.error("Failed to fetch categories.");
        }
      } catch (error) {
        toast.error("An error occurred while fetching categories.");
      }
    };

    fetchCategories();
  }, []);

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImageFile(file);
    }
  };

  const handleSpecificationChange = (e, key, type) => {
    const { value } = e.target;

    if (type === "key") {
      setSpecifications((prevSpecs) => {
        const updatedSpecs = { ...prevSpecs };
        const tempValue = updatedSpecs[key];
        delete updatedSpecs[key]; 
        updatedSpecs[value] = tempValue;
        return updatedSpecs;
      });
    } else if (type === "value") {
      setSpecifications((prevSpecs) => ({
        ...prevSpecs,
        [key]: value,
      }));
    }
  };

  const handleAddSpecification = () => {
    const newSpecKey = `Spec ${Object.keys(specifications).length + 1}`;
    setSpecifications({
      ...specifications,
      [newSpecKey]: "",
    });
  };

  const handleDeleteSpecification = (key) => {
    const updatedSpecs = { ...specifications };
    delete updatedSpecs[key];
    setSpecifications(updatedSpecs);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const newProduct = {
      title,
      price: parseFloat(price),
      stock: parseInt(stock),
      categoryId: category,
      specifications,
    };

    try {
      const response = await fetch("http://gccc1.eu-north-1.elasticbeanstalk.com:8081/api/v1/products", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newProduct),
      });

      if (response.ok) {
        const createdProduct = await response.json();

        if (imageFile) {
          const formData = new FormData();
          formData.append("image", imageFile);
          formData.append("productId", createdProduct.id);

          const uploadResponse = await fetch("http://gccc1.eu-north-1.elasticbeanstalk.com:8081/api/v1/products/upload-image", {
            method: "POST",
            body: formData,
          });

          if (uploadResponse.ok) {
            toast.success("Product created successfully and image uploaded!");
            navigate(`/product/${createdProduct.id}`);
          } else {
            toast.error("Product created, but image upload failed.");
          }
        } else {
          toast.success("Product created successfully without an image!");
          navigate(`/product/${createdProduct.id}`);
        }
      } else {
        const data = await response.json();
        toast.error(data.message || "Failed to create the product.");
      }
    } catch (error) {
      toast.error("An error occurred while creating the product.");
    }
  };

  return (
    <div className="edit-product-page">
      <h2>Create Product</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="create-product-title">Title</label>
          <input
            id="create-product-title"
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="create-product-price">Price</label>
          <input
            id="create-product-price"
            type="number"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="create-product-stock">Stock</label>
          <input
            id="create-product-stock"
            type="number"
            value={stock}
            onChange={(e) => setStock(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="create-product-category">Category</label>
          <select
            id="create-product-category"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            required
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
          <label htmlFor="create-product-specifications">Specifications</label>
          <div className="edit-product-specifications-inputs">
            {Object.keys(specifications).map((key, index) => (
              <div key={index} className="specification-item">
                <input
                  type="text"
                  value={key}
                  onChange={(e) => handleSpecificationChange(e, key, "key")} // Specify 'key'
                  placeholder="Specification Name"
                />
                <input
                  type="text"
                  value={specifications[key]}
                  onChange={(e) => handleSpecificationChange(e, key, "value")} // Specify 'value'
                  placeholder="Specification Value"
                />
                <button
                  type="button"
                  id="del-btn"
                  className="specification-delete-button"
                  onClick={() => handleDeleteSpecification(key)}
                >
                  Delete
                </button>
              </div>
            ))}
          </div>
          <button
            type="button"
            className="edit-product-add-specification-button"
            onClick={handleAddSpecification}
          >
            Add New Specification
          </button>
        </div>

        <div className="edit-product-image-upload">
          <label htmlFor="create-product-image" className="image-upload-area">
            <input
              id="create-product-image"
              type="file"
              accept="image/*"
              onChange={handleImageChange}
            />
          </label>

          {imageFile && (
            <>
              <img
                src={URL.createObjectURL(imageFile)}
                alt="Preview"
                className="image-preview"
              />
              <p className="selected-image-name">{imageFile.name}</p>
            </>
          )}
        </div>

        <button type="submit">Create Product</button>
      </form>
    </div>
  );
}

export default CreateProductPage;
