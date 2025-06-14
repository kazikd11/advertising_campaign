export default function Alert({ message, onClose }) {
    if (!message) return null;
    return (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-2 rounded relative mb-2">
            {message}
            {onClose && (
                <button
                    className="absolute top-0 right-0 px-2 py-1"
                    onClick={onClose}
                >
                    ×
                </button>
            )}
        </div>
    );
}
