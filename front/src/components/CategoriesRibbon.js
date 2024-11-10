import { useState, useEffect } from 'react';
import axios from 'axios';
import '../css/CategoriesRibbon.css';

function CategoriesRibbon() {
  const [categories, setCategories] = useState([]);
  const [page, setPage] = useState(0); 
  const [totalPages, setTotalPages] = useState(1); 

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get(`http://localhost:8081/api/v1/categories?page=${page}`);
        setCategories(response.data.content); 
        setTotalPages(response.data.totalPages); 
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, [page]);

  const handleNextPage = () => {
    if (page < totalPages - 1) {
      setPage(page + 1);
    }
  };

  const handlePrevPage = () => {
    if (page > 0) {
      setPage(page - 1);
    }
  };

  return (
    <div className="categories-ribbon">
      <button 
        className="prev-button" 
        onClick={handlePrevPage} 
        disabled={page === 0}
      >
        <i className="fas fa-arrow-left"></i>
      </button>

      <div className="categories-list">
        {categories.map((category) => (
          <div key={category.id} className="category-item">
            <span>{category.name}</span>
          </div>
        ))}
      </div>

      <button 
        className="next-button" 
        onClick={handleNextPage} 
        disabled={page === totalPages - 1}
      >
        <i className="fas fa-arrow-right"></i>
      </button>
    </div>
  );
}

export default CategoriesRibbon;
