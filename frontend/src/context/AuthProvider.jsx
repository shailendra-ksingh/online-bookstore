import { useState } from "react";
import { loginUser } from "../api/authApi";
import { AuthContext } from "./AuthContext";

const getStoredUser = () => {
    try {
        const savedUser = localStorage.getItem("user");
        return savedUser ? JSON.parse(savedUser) : null;

    } catch (error) {
        console.error("Failed to read stored user", error);
        localStorage.removeItem("user");
        return null;
    }
};

export function AuthProvider({ children }) {

    const [user, setUser] = useState(getStoredUser);

    const login = async (credentials) => {
        const loggedInUser = await loginUser(credentials);

        setUser(loggedInUser);
        localStorage.setItem("user", JSON.stringify(loggedInUser));

        return loggedInUser;
    };

    const logout = () => {
        setUser(null);
        localStorage.removeItem("user");
    };

    const value = {
        user,
        isAuthenticated: user !== null,
        login,
        logout
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}