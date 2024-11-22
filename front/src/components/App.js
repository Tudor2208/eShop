import { Toaster } from 'sonner';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './LoginPage';
import HomePage from './HomePage';
import ProductDetails from './ProductDetails';
import ShoppingCartPage from './ShoppingCartPage';

function App() {
  return (
    <>
      <Toaster richColors position="bottom-center" expand={true}/>
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/product/:id" element={<ProductDetails />} />
          <Route path="/cart" element={<ShoppingCartPage />} />
        </Routes>
    </Router>
    </>
  );
}

export default App;
