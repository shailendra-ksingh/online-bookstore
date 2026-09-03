import { useState } from "react";
import { useAuth } from "../context/useAuth";

function LoginPage({ onLoginSuccess, onRegisterClick }) {

    const { login } = useAuth();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();

        setError("");
        setLoading(true);

        try {

            await login({
                email,
                password
            });

            onLoginSuccess();

        } catch (error) {

            const message =
                error.response?.data?.message ||
                "Login failed";

            setError(message);

        } finally {

            setLoading(false);

        }
    };

    return (
        <div className="auth-page">

            <div className="auth-container">

                <div className="auth-card">

                    <h1>Login</h1>

                    <form onSubmit={handleSubmit}>

                        <div className="form-group">

                            <label>Email</label>

                            <input
                                type="email"
                                value={email}
                                onChange={(event) =>
                                    setEmail(event.target.value)
                                }
                                required
                            />

                        </div>

                        <div className="form-group">

                            <label>Password</label>

                            <input
                                type="password"
                                value={password}
                                onChange={(event) =>
                                    setPassword(event.target.value)
                                }
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
                            {loading ? "Logging in..." : "Login"}
                        </button>

                    </form>

                    <div className="auth-footer">

                        <p>Don't have an account?</p>

                        <button
                            type="button"
                            className="secondary-button"
                            onClick={onRegisterClick}
                        >
                            Create Account
                        </button>

                    </div>

                </div>

            </div>

        </div>
    );
}

export default LoginPage;