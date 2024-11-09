import { useState } from 'react';  
import '../css/LoginForm.css';

function LoginForm({ onSubmit }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordVisible, setPasswordVisible] = useState(false);  

  const handleSubmit = (e) => {
    e.preventDefault(); 

    onSubmit(email, password);
  };

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible); 
  };

  return (
    <div id="container">
      <div id="container-title">
        <h1>Login</h1>
      </div>

      <div id="container-form">
        <form onSubmit={handleSubmit}>
          <div className="row">
            <i className="fa-solid fa-envelope"></i>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="E-mail"
            />
          </div>

          <div className="row">
            <i className="fa-solid fa-lock"></i>
            <input
              type={passwordVisible ? 'text' : 'password'}  
              value={password}
              onChange={(e) => setPassword(e.target.value)} 
              placeholder="Password"
              id="password"
            />
            <i
              id="toggle-password"
              className={`fa-solid ${passwordVisible ? 'fa-eye-slash' : 'fa-eye'}`}  
              onClick={togglePasswordVisibility}  
            ></i>
          </div>

          <input type="submit" value="Login" id="submit-button" />
          <h3>
            Don't have an account? <a href="#">Sign up here!</a>
          </h3>
        </form>
      </div>
    </div>
  );
}

export default LoginForm;
