import { useState, useEffect } from "react";
import { apiFetch } from "../api";
import Alert from "./Alert";
import CampaignForm from "./CampaignForm";

export default function CampaignList({ userId, products, onChange }) {
    const [campaigns, setCampaigns] = useState([]);
    const [error, setError] = useState("");
    const [editing, setEditing] = useState(null);

    const fetchCampaigns = () => {
        apiFetch("/api/campaigns", { headers: { "X-USER-ID": userId } })
            .then(setCampaigns)
            .catch((e) => setError(e.message));
    };

    useEffect(fetchCampaigns, [userId]);

    const handleDelete = async (id) => {
        try {
            setError("");
            await apiFetch(`/api/campaigns/${id}`, {
                method: "DELETE",
                headers: { "X-USER-ID": userId },
            });
            fetchCampaigns();
            if (onChange) onChange();
        } catch (e) {
            setError(e.message);
        }
    };

    if (editing) {
        return (
            <CampaignForm
                userId={userId}
                products={products}
                initial={editing}
                onCancel={() => setEditing(null)}
                onSuccess={() => {
                    setEditing(null);
                    fetchCampaigns();
                    if (onChange) onChange();
                }}
            />
        );
    }

    return (
        <div>
            <Alert message={error} onClose={() => setError("")} />
            <div className="flex justify-between items-center mb-2">
                <div className="font-bold">Campaigns</div>
                <button
                    className="bg-blue-500 text-white px-3 py-2 rounded"
                    onClick={() => setEditing({})}
                >
                    Add
                </button>
            </div>
            <ul>
                {campaigns.map((c) => (
                    <li
                        key={c.id}
                        className="border-b py-2 flex justify-between items-center"
                    >
                        <div>
                            <div className="font-semibold">{c.name}</div>
                            <div className="text-sm text-gray-600">
                                Product:{" "}
                                {products.find((p) => p.id === c.productId)
                                    ?.name || "—"}
                                <br />
                                Status: {c.isActive ? "On" : "Off"}
                                <br />
                                Bid amount: {c.bidAmount}
                                <br />
                                Fund: {c.fund}
                                <br />
                                Town: {c.town?.name || "—"}
                                <br />
                                Radius: {c.radius} km
                                <br />
                                Keywords:{" "}
                                {c.keywords?.map((k) => k.keyword).join(", ") ||
                                    "—"}
                            </div>
                        </div>
                        <div className="flex gap-2">
                            <button
                                className="text-blue-500"
                                onClick={() => setEditing(c)}
                            >
                                Edit
                            </button>
                            <button
                                className="text-red-500"
                                onClick={() => handleDelete(c.id)}
                            >
                                Delete
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}
