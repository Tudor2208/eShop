import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ component: Component, requiredAdmin, requiredNotLoggedIn, ...rest }) => {
  const user = JSON.parse(localStorage.getItem('user')); 

  if (requiredNotLoggedIn && user) {
    return <Navigate to="/" />;
  }

  if (requiredAdmin && (!user || !user.isAdmin)) {
    return <Navigate to="/unauthorized" />;
  }

  if (!user && !requiredNotLoggedIn) {
    return <Navigate to="/login" />;
  }

  return <Component {...rest} />;
};

export default ProtectedRoute;
