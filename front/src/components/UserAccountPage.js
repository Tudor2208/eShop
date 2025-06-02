import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from 'sonner';
import '../css/UserAccountPage.css';

function UserAccountPage() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [password, setPassword] = useState('');

  const userEmail = (() => {
    try {
      const storedUser = JSON.parse(localStorage.getItem("user"));
      return storedUser?.email || null;
    } catch (error) {
      console.error("Error retrieving user email:", error);
      return null;
    }
  })();

  useEffect(() => {
    if (!userEmail) {
      toast.error("User not logged in.");
      navigate("/login");
      return;
    }

    const fetchUser = async () => {
      try {
        const response = await fetch(`http://gccc.eu-north-1.elasticbeanstalk.com:8080/api/v1/users/${userEmail}`);
        if (response.ok) {
          const userData = await response.json();
          setUser(userData);
          setFirstName(userData.firstName);
          setLastName(userData.lastName);
          setPhoneNumber(userData.phoneNumber);
          setPassword('');
        } else {
          toast.error("Failed to fetch user details.");
        }
      } catch (error) {
        toast.error("An error occurred while fetching user details.");
      }
    };

    fetchUser();
  }, [userEmail, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!userEmail) {
      toast.error("Cannot update details without a valid user.");
      return;
    }

    const updatedUser = {
      firstName,
      lastName,
      phoneNumber,
      ...(password && { password }), 
    };

    try {
      const response = await fetch(`http://gccc.eu-north-1.elasticbeanstalk.com:8080/api/v1/users/${userEmail}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedUser),
      });

      if (response.ok) {
        toast.success("Account updated successfully!");
        navigate(`/`); 
      } else {
        const data = await response.json();
        toast.error(data.message || "Failed to update the account.");
      }
    } catch (error) {
      toast.error("An error occurred while updating the account.");
    }
  };

  if (!user) return <div>Loading...</div>;

  return (
    <div className="edit-account-page">
      <h2>Edit Account</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="edit-user-first-name">First Name</label>
          <input
            id="edit-user-first-name"
            type="text"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
        </div>

        <div>
          <label htmlFor="edit-user-last-name">Last Name</label>
          <input
            id="edit-user-last-name"
            type="text"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
        </div>

        <div>
          <label htmlFor="edit-user-phone-number">Phone Number</label>
          <input
            id="edit-user-phone-number"
            type="text"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
          />
        </div>

        <div>
          <label htmlFor="edit-user-password">Password</label>
          <input
            id="edit-user-password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter new password"
          />
        </div>

        <button type="submit">Save Changes</button>
      </form>
    </div>
  );
}

export default UserAccountPage;
