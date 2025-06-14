import { useState } from "react";
import UserSelect from "./components/UserSelect";
import ClientPanel from "./components/ClientPanel";

function App() {
    const [user, setUser] = useState(null);

    return (
        <div className="container mx-auto p-4">
            {!user ? (
                <UserSelect onSelect={setUser} />
            ) : (
                <ClientPanel user={user} onLogout={() => setUser(null)} />
            )}
        </div>
    );
}

export default App;
