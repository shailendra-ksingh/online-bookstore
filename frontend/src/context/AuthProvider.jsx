import { useState } from "react";
import { loginUser } from "../api/authApi";
import { AuthContext } from "./AuthContext";

export function AuthProvider({ children }) {

    const [user, setUser] = useState(() => {

        const savedUser = localStorage.getItem("user");

        return savedUser ? JSON.parse(savedUser) : null;
    });


    const login = async (credentials) => {

        const loggedInUser = await loginUser(credentials);

        // Save in React state
        setUser(loggedInUser);

        // Save in browser storage
        localStorage.setItem(
            "user",
            JSON.stringify(loggedInUser)
        );

        return loggedInUser;
    };


    const logout = () => {

        // Clear React state
        setUser(null);

        // Clear browser storage
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