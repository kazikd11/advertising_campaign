import { useState, useEffect } from "react";
import { apiFetch } from "../api";
import Alert from "./Alert";

export default function UserSelect({ onSelect }) {
    const [users, setUsers] = useState([]);
    const [username, setUsername] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        apiFetch("/api/users")
            .then(setUsers)
            .catch((e) => setError(e.message));
    }, []);

    const handleAdd = async () => {
        try {
            setError("");
            const user = await apiFetch(
                `/api/users?username=${encodeURIComponent(username)}`,
                { method: "POST" }
            );
            setUsers([...users, user]);
            setUsername("");
            onSelect(user);
        } catch (e) {
            setError(e.message);
        }
    };

    return (
        <div className="max-w-md mx-auto mt-10">
            <Alert message={error} onClose={() => setError("")} />
            <div className="mb-4">
                <label className="block mb-2">Select user:</label>
                <select
                    className="w-full border p-2 rounded"
                    onChange={(e) => {
                        const user = users.find(
                            (u) => u.id === Number(e.target.value)
                        );
                        if (user) onSelect(user);
                    }}
                >
                    <option value="">-- Select user --</option>
                    {users.map((u) => (
                        <option key={u.id} value={u.id}>
                            {u.username}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <label className="block mb-2">Or add new user:</label>
                <div className="flex gap-x-2 h-10">
                <input
                    className="border p-2 w-full rounded"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Username"
                />
                <button
                    className="bg-blue-500 text-white px-4 rounded"
                    onClick={handleAdd}
                    disabled={!username}
                >
                    Add
                </button>
                </div>
            </div>
        </div>
    );
}
