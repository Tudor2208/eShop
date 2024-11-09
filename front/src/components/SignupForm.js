import { useState } from 'react';  
import '../css/SignupForm.css';
import { toast } from 'sonner';


function SignupForm(props) {
  const [email, setEmail] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault(); 

    if (password !== confirmPassword) {
      toast.error("Passwords do not match!");
      return;
    }

    props.onSubmit({
      email,
      firstName,
      lastName,
      phone,
      password,
    });
  };

  return (
    <div id="container-signup">
      <div id="container-title-signup">
        <h1>Sign Up</h1>
      </div>

      <div id="container-form-signup">
        <form onSubmit={handleSubmit}>
          <div className="row-signup">
            <i className="fa-solid fa-envelope"></i>
            <input
              required
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="E-mail"
            />
          </div>

          <div className="row-signup">
            <i className="fa-solid fa-user"></i>
            <input
              required
              type="text"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              placeholder="First Name"
            />
          </div>

          <div className="row-signup">
            <i className="fa-solid fa-user"></i>
            <input
              required
              type="text"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              placeholder="Last Name"
            />
          </div>

          <div className="row-signup">
            <i className="fa-solid fa-phone"></i>
            <input
              required
              type="tel"
              value={phone}
              onChange={(e) => setPhone(e.target.value)}
              placeholder="Phone Number"
            />
          </div>

          <div className="row-signup">
            <i className="fa-solid fa-lock"></i>
            <input
              required
              type='password' 
              value={password}
              onChange={(e) => setPassword(e.target.value)} 
              placeholder="Password"
              id="password-signup"
            />
        
          </div>

          <div className="row-signup">
            <i className="fa-solid fa-lock"></i>
            <input
              required
              type='password' 
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)} 
              placeholder="Confirm Password"
              id="confirm-password-signup"
            />
          </div>

          <input type="submit" value="Sign Up" id="submit-button-signup" />
        </form>
      </div>
    </div>
  );
}

export default SignupForm;
