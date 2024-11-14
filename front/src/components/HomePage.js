import Navbar from "./Navbar";
import Products from "./Products";
import { useState } from 'react';

function HomePage() {
    const [categoryId, setCategoryId] = useState(null);
    const [searchText, setSearchText] = useState(null);

    const handleCategorySelect = (selectedCategoryId) => {
        setCategoryId(selectedCategoryId);
    };

    const handleSearchText = (text) => {
        setSearchText(text);
    };

    return (
        <>
             <Navbar onCategorySelect={handleCategorySelect} onSearchText={handleSearchText} />
             <Products categoryId={categoryId} searchText={searchText} />
        </>
    )
}

export default HomePage