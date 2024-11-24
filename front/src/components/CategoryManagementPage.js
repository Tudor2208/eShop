import React, { useState, useEffect } from 'react';
import { toast } from 'sonner';  // For notifications
import '../css/CategoryManagementPage.css';

function CategoryManagementPage() {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState('');
  
  useEffect(() => {
    // Fetch all categories when the component mounts
    const fetchCategories = async () => {
      try {
        const response = await fetch('http://localhost:8081/api/v1/categories');
        if (response.ok) {
          const data = await response.json();
          setCategories(data.content);  // Store the fetched categories
        } else {
          toast.error('Failed to fetch categories');
        }
      } catch (error) {
        toast.error('An error occurred while fetching categories');
      }
    };
    fetchCategories();
  }, []);

  const handleCreateCategory = async (e) => {
    e.preventDefault();
    if (!newCategory.trim()) {
      toast.error('Category name cannot be empty');
      return;
    }

    const category = { name: newCategory };

    try {
      const response = await fetch('http://localhost:8081/api/v1/categories', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(category),
      });

      if (response.ok) {
        const createdCategory = await response.json();
        setCategories([...categories, createdCategory]);
        toast.success('Category created successfully');
        setNewCategory('');  // Clear the input
      } else {
        const data = await response.json();
        toast.error(data.message || 'Failed to create category');
      }
    } catch (error) {
      toast.error('An error occurred while creating the category');
    }
  };

  const handleDeleteCategory = async (id) => {
    try {
      const response = await fetch(`http://localhost:8081/api/v1/categories/${id}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        setCategories(categories.filter((category) => category.id !== id));
        toast.success('Category deleted successfully');
      } else {
        toast.error('Failed to delete category');
      }
    } catch (error) {
      toast.error('An error occurred while deleting the category');
    }
  };

  return (
    <div className="category-management-page">
      <h2 id="category-title">Category Management</h2>
      
      <div className="management-create-category-form">
        <form onSubmit={handleCreateCategory}>
          <input
            type="text"
            value={newCategory}
            onChange={(e) => setNewCategory(e.target.value)}
            placeholder="Category Name"
            required
          />
          <button type="submit">Create</button>
        </form>
      </div>

      <div className="management-category-list">
        <ul>
          {categories.map((category) => (
            <li key={category.id} className="management-category-item">
              <span>{category.name}</span>
              <button
                onClick={() => handleDeleteCategory(category.id)}
                className="management-delete-button"
              >
                Delete
              </button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default CategoryManagementPage;
