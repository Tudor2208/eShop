import { Toaster } from 'sonner';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './LoginPage';
import HomePage from './HomePage';
import ProductDetails from './ProductDetails';
import ShoppingCartPage from './ShoppingCartPage';
import OrdersPage from './OrdersPage';
import ProtectedRoute from './ProtectedRoute';
import ProductEditPage from './ProductEditPage';
import UnauthorizedPage from './UnauthorizedPage';
import SignupForm from './SignupForm';
import CreateProductPage from './CreateProductPage';
import CategoryManagementPage from './CategoryManagementPage';
import UserAccountPage from './UserAccountPage';

function App() {
  return (
    <>
      <Toaster richColors position="bottom-center" expand={true} />
      <Router>
        <Routes>
          <Route
            path="/login"
            element={<ProtectedRoute requiredNotLoggedIn  component={LoginPage} />}
          />
          <Route path="/signup" element={<SignupForm />} />
          <Route path="/unauthorized" element={<UnauthorizedPage />} />
          <Route
            path="/"
            element={<ProtectedRoute component={HomePage} />}
          />
          <Route
            path="/product/:id"
            element={<ProtectedRoute component={ProductDetails} />}
          />
           <Route
            path="/product/edit/:id"
            element={<ProtectedRoute requiredAdmin component={ProductEditPage} />}
          />
          <Route
            path="/cart"
            element={<ProtectedRoute component={ShoppingCartPage} />}
          />
          <Route
            path="/orders"
            element={<ProtectedRoute component={OrdersPage} />}
          />
          <Route
            path="/create-product"
            element={<ProtectedRoute requiredAdmin component={CreateProductPage} />}
          />
          <Route
            path="/categories"
            element={<ProtectedRoute requiredAdmin component={CategoryManagementPage} />}
          />
           <Route
            path="/account"
            element={<ProtectedRoute component={UserAccountPage} />}
          />
        </Routes>
      </Router>
    </>
  );
}

export default App;
