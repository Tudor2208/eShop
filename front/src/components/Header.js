import '../css/Header.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Header(props) {
    const [searchText, setSearchText] = useState('');
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('user');
        navigate("/login");
    };

    const handleSearch = () => {
        props.onSearchText(searchText);
        setSearchText(''); 
    };

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') { 
            handleSearch(); 
        }
    };

    return (
        <header className="header">
            <div 
                className="logo"
                onClick={() => navigate("/")}>
                <span>eSHOP</span>
            </div>
            
            <div className="search-bar">
                <input 
                    type="text" 
                    placeholder="Search for products..."
                    value={searchText} 
                    onChange={(e) => setSearchText(e.target.value)}
                    onKeyDown={handleKeyDown} 
                    autoComplete="off" 
                />
                <button 
                    className="search-icon" 
                    onClick={handleSearch}
                >
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
                <button className="logout-icon" title="Logout" onClick={handleLogout}>
                    <i className="fas fa-sign-out-alt"></i>
                </button>
            </div>
        </header>
    );
}

export default Header;
