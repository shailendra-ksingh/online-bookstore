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

        setForm((previous) => ({
            ...previous,
            [name]: value
        }));
    };


    const handleSubmit = async (event) => {
        event.preventDefault();

        setError("");
        setSuccess("");
        setLoading(true);

        try {
            await registerUser(form);

            setSuccess(
                "Registration successful. Please login."
            );

            setForm({
                name: "",
                email: "",
                password: ""
            });

        } catch (error) {

            const message =
                error.response?.data?.password ||
                error.response?.data?.message ||
                "Registration failed";

            setError(message);

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

                                <label>Name</label>

                                <input
                                    type="text"
                                    name="name"
                                    value={form.name}
                                    onChange={handleChange}
                                    required
                                />

                            </div>


                            <div className="form-group">

                                <label>Email</label>

                                <input
                                    type="email"
                                    name="email"
                                    value={form.email}
                                    onChange={handleChange}
                                    required
                                />

                            </div>


                            <div className="form-group">

                                <label>Password</label>

                                <input
                                    type="password"
                                    name="password"
                                    value={form.password}
                                    onChange={handleChange}
                                    minLength="6"
                                    required
                                />

                            </div>


                            {error && (
                                <p className="error-message">
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