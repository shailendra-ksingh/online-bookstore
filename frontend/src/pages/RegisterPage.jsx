import { useState } from "react";
import { registerUser } from "../api/authApi";

function RegisterPage({ onRegistrationComplete }) {
    const [form, setForm] = useState({
        name: "",
        email: "",
        password: ""
    });

    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setForm((previousForm) => ({
            ...previousForm,
            [name]: value
        }));
    };

    const getErrorMessage = (error) => {
        const errorData = error.response?.data;

        // Sometimes the backend sends the error directly as text.
        if (typeof errorData === "string") {
            return errorData;
        }

        // Pick the validation message based on the field returned by the API.
        return (
            errorData?.name ||
            errorData?.email ||
            errorData?.password ||
            errorData?.message ||
            "Unable to create account. Please try again."
        );
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        setError("");
        setSuccess("");
        setLoading(true);

        try {
            await registerUser({
                name: form.name.trim(),
                email: form.email.trim(),
                password: form.password
            });

            setSuccess("Registration successful. Please login.");

            setForm({
                name: "",
                email: "",
                password: ""
            });
        } catch (error) {
            setError(getErrorMessage(error));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-container">
                <div className="auth-card">
                    <h1>Create Account</h1>

                    {success ? (
                        <div className="success-section">
                            <p className="success-message">
                                {success}
                            </p>

                            <button
                                type="button"
                                onClick={onRegistrationComplete}
                            >
                                Go to Login
                            </button>
                        </div>
                    ) : (
                        <form onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label htmlFor="name">
                                    Name
                                </label>

                                <input
                                    id="name"
                                    type="text"
                                    name="name"
                                    value={form.name}
                                    onChange={handleChange}
                                    autoComplete="name"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="email">
                                    Email
                                </label>

                                <input
                                    id="email"
                                    type="email"
                                    name="email"
                                    value={form.email}
                                    onChange={handleChange}
                                    autoComplete="email"
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="password">
                                    Password
                                </label>

                                <input
                                    id="password"
                                    type="password"
                                    name="password"
                                    value={form.password}
                                    onChange={handleChange}
                                    autoComplete="new-password"
                                    minLength={8}
                                    required
                                />
                            </div>

                            {error && (
                                <p
                                    className="error-message"
                                    role="alert"
                                >
                                    {error}
                                </p>
                            )}

                            <button
                                type="submit"
                                disabled={loading}
                            >
                                {loading
                                    ? "Creating account..."
                                    : "Register"}
                            </button>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
}

export default RegisterPage;