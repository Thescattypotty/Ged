import { Route, Routes } from "react-router";


export default function AppRoutes(){

    return (
        <Routes>
            <Route path="/" element={<div>Home</div>} />
        </Routes>
    );
}