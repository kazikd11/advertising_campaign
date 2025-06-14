export async function apiFetch(path, options = {}) {
    const headers = { ...options.headers };
    if (options.body && options.method !== "DELETE") {
        headers["Content-Type"] = "application/json";
    }
    const res = await fetch(path, { ...options, headers });
    if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Server error");
    }
    return res.status === 204 ? null : res.json();
}
