import '../css/Header.css';
import { useState } from 'react';

function Header(props) {

    const [searchText, setSearchText] = useState('')

    return (
        <header className="header">
            <div className="logo">
                <span>eSHOP</span>
            </div>
            
            <div className="search-bar">
                <input type="text" 
                       placeholder="Search for products..."
                       onChange={(e) => setSearchText(e.target.value)} />
                <button className="search-icon" 
                        onClick={() => props.onClick(searchText)}>
                    <i className="fas fa-search"></i>
                </button>
            </div>
            
            <div className="nav-buttons">
                <button className="nav-button">
                    <i className="fas fa-user left-icon"></i> 
                    My account
                    <i className="fas fa-arrow-down right-icon"></i> 
                </button>
                <button className="nav-button">
                    <i className="fas fa-box left-icon"></i> 
                    My orders
                    <i className="fas fa-arrow-down right-icon"></i>
                </button>
                <button className="nav-button">
                    <i className="fas fa-shopping-cart left-icon"></i> 
                    My cart
                    <i className="fas fa-arrow-down right-icon"></i> 
                </button>
            </div>

            <div className="logout">
                <button className="logout-icon" title="Logout">
                    <i className="fas fa-sign-out-alt"></i>
                </button>
            </div>
        </header>
    );
}

export default Header;
