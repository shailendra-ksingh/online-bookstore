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
                email: email.trim(),
                password
            });

            onLoginSuccess();

        } catch (error) {

            const message =
                error.response?.data?.message ||
                "Unable to login. Please check your credentials.";

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

                            <label htmlFor="email">
                                Email
                            </label>

                            <input
                                id="email"
                                type="email"
                                value={email}
                                onChange={(event) =>
                                    setEmail(event.target.value)
                                }
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
                                value={password}
                                onChange={(event) =>
                                    setPassword(event.target.value)
                                }
                                autoComplete="current-password"
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
                                ? "Logging in..."
                                : "Login"}
                        </button>

                    </form>


                    <div className="auth-footer">

                        <p>Don't have an account?</p>

                        <button
                            type="button"
                            className="secondary-button"
                            onClick={onRegisterClick}
                            disabled={loading}
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