import '../css/Header.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function Header(props) {
    const [searchText, setSearchText] = useState('');
    const [cartItemCount, setCartItemCount] = useState(0); 
    const navigate = useNavigate();
    const user = JSON.parse(localStorage.getItem("user"));

    const calculateCartItemCount = () => {
        const cart = JSON.parse(localStorage.getItem('cart')) || []; 
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0); 
        setCartItemCount(totalItems);
    };

    useEffect(() => {
        calculateCartItemCount(); 
    }, []); 

    useEffect(() => {
        window.addEventListener('storage', calculateCartItemCount); 
        return () => window.removeEventListener('storage', calculateCartItemCount); 
    }, []);

    const updateCartInLocalStorage = (updatedCart) => {
        localStorage.setItem('cart', JSON.stringify(updatedCart)); 
        calculateCartItemCount();  
    };

    const handleLogout = () => {
        localStorage.removeItem('user');
        localStorage.removeItem('cart');
        navigate("/login");
    };

    const handleCart = () => {
        navigate("/cart");
    };

    const handleOrders = () => {
        navigate("/orders");
    };

    const handleAccount = () => {
        navigate("/account");
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
            
            {!user.isAdmin && (
                 <div className="nav-buttons">
                 <button className="nav-button" onClick={handleAccount}>
                     <i className="fas fa-user left-icon"></i> 
                     My account
                     <i className="fas fa-arrow-down right-icon"></i> 
                 </button>
                 <button className="nav-button" onClick={handleOrders}>
                     <i className="fas fa-box left-icon"></i> 
                     My orders
                     <i className="fas fa-arrow-down right-icon"></i>
                 </button>
                 <button className="nav-button" onClick={handleCart}>
                     <i className="fas fa-shopping-cart left-icon"></i> 
                     My cart
                     <i className="fas fa-arrow-down right-icon"></i> 
                     {cartItemCount > 0 && (
                         <div className="cart-badge">
                             {cartItemCount}
                         </div>
                     )}
                 </button>
             </div>
            )}

            <div className="logout">
                <button className="logout-icon" title="Logout" onClick={handleLogout}>
                    <i className="fas fa-sign-out-alt"></i>
                </button>
            </div>
        </header>
    );
}

export default Header;
