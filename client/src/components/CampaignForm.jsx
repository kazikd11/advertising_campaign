import { useState, useEffect } from "react";
import { apiFetch } from "../api";
import Alert from "./Alert";
import { useRef } from "react";

export default function CampaignForm({
    userId,
    products,
    initial = {},
    onCancel,
    onSuccess,
}) {
    const [towns, setTowns] = useState([]);
    const [keywords, setKeywords] = useState([]);
    const [form, setForm] = useState({
        productId: initial.productId || (products[0]?.id ?? ""),
        name: initial.name || "",
        keywordIds: initial.keywords?.map((k) => k.id) || [],
        bidAmount: initial.bidAmount || "",
        fund: initial.fund || "",
        isActive: initial.isActive ?? true,
        townId: initial.town?.id || "",
        radius: initial.radius || "",
    });
    const [kwQuery, setKwQuery] = useState("");
    const [kwOptions, setKwOptions] = useState([]);
    const [error, setError] = useState("");
    const isEdit = !!initial.id;
    const [allKeywords, setAllKeywords] = useState([]);

    const [kwFocused, setKwFocused] = useState(false);
    const inputRef = useRef(null);

    useEffect(() => {
        apiFetch("/api/towns")
            .then(setTowns)
            .catch(() => {});
    }, []);

    useEffect(() => {
        apiFetch("/api/keywords")
            .then(setAllKeywords)
            .catch(() => {});
    }, []);

    useEffect(() => {
        apiFetch(`/api/keywords?query=${encodeURIComponent(kwQuery)}`)
            .then(setKwOptions)
            .catch(() => {});
    }, [kwQuery]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setForm((f) => ({
            ...f,
            [name]: type === "checkbox" ? checked : value,
        }));
    };

    const handleKwFocus = () => {
        setKwFocused(true);
        if (!kwQuery && kwOptions.length === 0) {
            apiFetch("/api/keywords")
                .then(setKwOptions)
                .catch(() => {});
        }
    };

    const handleKwBlur = () => {
        setTimeout(() => setKwFocused(false), 100);
    };

    const handleAddKeyword = (k) => {
        if (!form.keywordIds.includes(k.id)) {
            setForm((f) => ({ ...f, keywordIds: [...f.keywordIds, k.id] }));
        }
        setKwQuery("");
        setKwOptions([]);
    };

    const handleRemoveKeyword = (id) => {
        setForm((f) => ({
            ...f,
            keywordIds: f.keywordIds.filter((kid) => kid !== id),
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setError("");
            const payload = {
                ...form,
                bidAmount: Number(form.bidAmount),
                fund: Number(form.fund),
                radius: Number(form.radius),
                isActive: !!form.isActive,
                keywordIds: form.keywordIds,
            };
            if (isEdit) {
                await apiFetch(`/api/campaigns/${initial.id}`, {
                    method: "PUT",
                    headers: { "X-USER-ID": userId },
                    body: JSON.stringify(payload),
                });
            } else {
                await apiFetch("/api/campaigns", {
                    method: "POST",
                    headers: { "X-USER-ID": userId },
                    body: JSON.stringify(payload),
                });
            }
            onSuccess();
        } catch (e) {
            setError(e.message);
        }
    };

    return (
        <form
            className="max-w-lg mx-auto my-6 p-4 border rounded bg-white"
            onSubmit={handleSubmit}
        >
            <Alert message={error} onClose={() => setError("")} />
            <div className="mb-2">
                <label className="block mb-1">Product</label>
                <select
                    name="productId"
                    className="w-full border p-2"
                    value={form.productId}
                    onChange={handleChange}
                    required
                >
                    {products.map((p) => (
                        <option key={p.id} value={p.id}>
                            {p.name}
                        </option>
                    ))}
                </select>
            </div>
            <div className="mb-2">
                <label className="block mb-1">Campaign name</label>
                <input
                    name="name"
                    className="w-full border p-2"
                    value={form.name}
                    onChange={handleChange}
                    required
                />
            </div>
            <div className="mb-2">
                <label className="block mb-1">Keywords</label>
                <div className="flex gap-2 mb-1 relative">
                    <input
                        ref={inputRef}
                        className="border p-2 flex-1"
                        value={kwQuery}
                        onChange={(e) => setKwQuery(e.target.value)}
                        placeholder="Type to search..."
                        onFocus={handleKwFocus}
                        onBlur={handleKwBlur}
                    />
                    {kwFocused && kwOptions.length > 0 && (
                        <div className="absolute bg-white border mt-10 z-10 w-64 max-h-48 overflow-auto">
                            {kwOptions.map((k) => (
                                <div
                                    key={k.id}
                                    className="p-2 hover:bg-gray-100 cursor-pointer"
                                    onMouseDown={() => handleAddKeyword(k)}
                                >
                                    {k.keyword}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
                <div className="flex flex-wrap gap-2">
                    {form.keywordIds.map((id) => {
                        const k = allKeywords.find((k) => k.id === id);
                        return (
                            <span
                                key={id}
                                className="bg-blue-100 px-2 py-1 rounded"
                            >
                                {k ? k.keyword : id}
                                <button
                                    type="button"
                                    className="ml-1 text-red-500"
                                    onClick={() => handleRemoveKeyword(id)}
                                >
                                    ×
                                </button>
                            </span>
                        );
                    })}
                </div>
            </div>
            <div className="mb-2">
                <label className="block mb-1">Bid amount</label>
                <input
                    name="bidAmount"
                    type="number"
                    min="0.01"
                    step="0.01"
                    className="w-full border p-2"
                    value={form.bidAmount}
                    onChange={handleChange}
                    required
                />
            </div>
            <div className="mb-2">
                <label className="block mb-1">Fund</label>
                <input
                    name="fund"
                    type="number"
                    min="0.01"
                    step="0.01"
                    className="w-full border p-2"
                    value={form.fund}
                    onChange={handleChange}
                    required
                />
            </div>
            <div className="mb-2">
                <label className="block mb-1">Status</label>
                <select
                    name="isActive"
                    className="w-full border p-2"
                    value={form.isActive ? "on" : "off"}
                    onChange={(e) =>
                        setForm((f) => ({
                            ...f,
                            isActive: e.target.value === "on",
                        }))
                    }
                >
                    <option value="on">On</option>
                    <option value="off">Off</option>
                </select>
            </div>
            <div className="mb-2">
                <label className="block mb-1">Town</label>
                <select
                    name="townId"
                    className="w-full border p-2"
                    value={form.townId}
                    onChange={handleChange}
                    required
                >
                    <option value="">-- Select town --</option>
                    {towns.map((t) => (
                        <option key={t.id} value={t.id}>
                            {t.name}
                        </option>
                    ))}
                </select>
            </div>
            <div className="mb-2">
                <label className="block mb-1">Radius (km)</label>
                <input
                    name="radius"
                    type="number"
                    min="1"
                    className="w-full border p-2"
                    value={form.radius}
                    onChange={handleChange}
                    required
                />
            </div>
            <div className="flex gap-2 mt-4">
                <button
                    type="submit"
                    className="bg-blue-600 text-white px-4 py-2 rounded"
                >
                    {isEdit ? "Update" : "Create"}
                </button>
                <button
                    type="button"
                    className="bg-gray-300 px-4 py-2 rounded"
                    onClick={onCancel}
                >
                    Cancel
                </button>
            </div>
        </form>
    );
}
