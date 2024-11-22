import LoginForm from "./LoginForm";
import '../css/LoginPage.css';
import { toast } from 'sonner';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
    const navigate = useNavigate();

    const handleLogin = async (email, password) => {
        const url = 'http://localhost:8080/api/v1/users/login';

        const requestBody = {
            email: email,
            password: password
        };

        try {
            const response = await fetch(url, {
                method: 'POST', 
                headers: {
                    'Content-Type': 'application/json', 
                },
                body: JSON.stringify(requestBody), 
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('user', JSON.stringify(data));

                // Fetch the user's cart after successful login
                const cartResponse = await fetch(`http://localhost:8082/api/v1/carts/${email}`);
                if (cartResponse.ok) {
                    const cartData = await cartResponse.json();
                    localStorage.setItem('cart', JSON.stringify(cartData.items || [])); // Store cart items in local storage
                } else {
                    toast.error('Failed to fetch the cart.');
                }

                navigate("/"); // Redirect to home page
                toast.success("Login successful");
            } else {
                toast.error('Invalid credentials!'); 
            }

        } catch (error) {
            console.error('Error during login:', error);
            toast.error('An error occurred while logging in.');
        }
    }

    return (
        <div className="login-page">
            <LoginForm onSubmit={handleLogin}/>
        </div>
    );
}

export default LoginPage;
