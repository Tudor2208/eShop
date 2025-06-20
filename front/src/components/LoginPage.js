import LoginForm from "./LoginForm";
import '../css/LoginPage.css';
import { toast } from 'sonner';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
    const navigate = useNavigate();

    const handleLogin = async (email, password) => {
        const url = 'http://gccc.eu-north-1.elasticbeanstalk.com:8080/api/v1/users/login';

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

                const cartResponse = await fetch(`http://gccc2.eu-north-1.elasticbeanstalk.com:8082/api/v1/carts/${email}`);
                if (cartResponse.ok) {
                    const cartData = await cartResponse.json();
                    localStorage.setItem('cart', JSON.stringify(cartData.items || [])); 
                } else {
                    toast.error('Failed to fetch the cart.');
                }

                navigate("/"); 
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
