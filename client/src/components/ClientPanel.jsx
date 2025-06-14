import { useState, useEffect } from "react";
import { apiFetch } from "../api";
import Alert from "./Alert";
import ProductList from "./ProductList";
import CampaignList from "./CampaignList";

export default function ClientPanel({ user, onLogout }) {
    const [details, setDetails] = useState(null);
    const [error, setError] = useState("");
    const [amount, setAmount] = useState("");

    const fetchDetails = () => {
        apiFetch("/api/users/me", { headers: { "X-USER-ID": user.id } })
            .then(setDetails)
            .catch((e) => setError(e.message));
    };

    useEffect(fetchDetails, [user]);

    const handleAddFunds = async () => {
        try {
            setError("");
            await apiFetch(`/api/users/add-funds?amount=${amount}`, {
                method: "PUT",
                headers: { "X-USER-ID": user.id },
            });
            setAmount("");
            fetchDetails();
        } catch (e) {
            setError(e.message);
        }
    };

    if (!details) return <div>Loading...</div>;

    return (
        <div>
            <Alert message={error} onClose={() => setError("")} />
            <div className="flex justify-between items-center mb-4">
                <div>
                    <span className="font-bold">{details.username}</span> |
                    Balance:{" "}
                    <span className="font-mono">{details.balance}</span> <img src="/emerald.png" alt="emerald" className="inline-block w-3 h-3 mb-0.5" />
                </div>
                <button className="text-blue-600" onClick={onLogout}>
                    Logout
                </button>
            </div>
            <div className="mb-4 flex items-center gap-2">
                <input
                    type="number"
                    min="0.01"
                    step="0.01"
                    className="border p-2 rounded"
                    placeholder="Add emeralds"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                />
                <button
                    className="bg-green-500 text-white px-3 py-2 rounded"
                    onClick={handleAddFunds}
                    disabled={!amount}
                >
                    Add
                </button>
            </div>
            <ProductList
                userId={user.id}
                onChange={fetchDetails}
                products={details.products}
            />
            <CampaignList
                userId={user.id}
                products={details.products}
                onChange={fetchDetails}
            />
        </div>
    );
}
