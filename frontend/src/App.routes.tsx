import { Route, Routes } from "react-router";
import DashboardLayout from "./layout/layout";
import FolderList from "./pages/folder/list";


export default function AppRoutes(){

    return (
        <Routes>
            <Route path="/" element={
                <DashboardLayout />
            }>
                <Route index element={<div>Home Page</div>} />
                <Route path="folders" element={<FolderList />} />
                <Route path="files" element={<div>Files Page</div>} />
                <Route path="tags" element={<div>Tags Page</div>} />
                <Route path="*" element={<div>404 Not Found</div>} />
            </Route>
        </Routes>
    );
}