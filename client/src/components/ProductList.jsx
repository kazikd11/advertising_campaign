import { useState } from "react";
import { apiFetch } from "../api";
import Alert from "./Alert";

export default function ProductList({ userId, products, onChange }) {
    const [name, setName] = useState("");
    const [error, setError] = useState("");

    const handleAdd = async () => {
        try {
            setError("");
            await apiFetch(`/api/products?name=${encodeURIComponent(name)}`, {
                method: "POST",
                headers: { "X-USER-ID": userId },
            });
            setName("");
            onChange();
        } catch (e) {
            setError(e.message);
        }
    };

    const handleDelete = async (id) => {
        try {
            setError("");
            await apiFetch(`/api/products/${id}`, {
                method: "DELETE",
                headers: { "X-USER-ID": userId },
            });
            onChange();
        } catch (e) {
            setError(e.message);
        }
    };

    return (
        <div className="mb-6">
            <Alert message={error} onClose={() => setError("")} />
            <div className="mb-2 font-bold">Products</div>
            <ul className="mb-2">
                {products.map((p) => (
                    <li
                        key={p.id}
                        className="flex justify-between items-center border-b border-gray-500 py-1"
                    >
                        <span>{p.name}</span>
                        <button
                            className="text-red-500"
                            onClick={() => handleDelete(p.id)}
                        >
                            Delete
                        </button>
                    </li>
                ))}
            </ul>
            <div className="flex gap-2">
                <input
                    className="border p-2 flex-1 rounded"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="New product name"
                />
                <button
                    className="bg-blue-500 text-white px-3 py-2 rounded"
                    onClick={handleAdd}
                    disabled={!name}
                >
                    Add
                </button>
            </div>
        </div>
    );
}
