import CategoriesRibbon from "./CategoriesRibbon";
import Header from "./Header";
import { useState } from 'react';
import React from "react";

function Navbar({ onCategorySelect, onSearchText }) {
    
    const handleCategoryClick = (categoryId) => {
        onCategorySelect(categoryId);
    }

    const handleSearch = (text) => {
        onSearchText(text); 
    }

    return(
        <>
            <Header onSearchText={handleSearch}/>
            <CategoriesRibbon onCategoryClick={handleCategoryClick}/>
        </>
    )
}

export default Navbar