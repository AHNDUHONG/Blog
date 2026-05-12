import {Navigate, Route, Routes} from "react-router-dom";
import PostListPage from "./pages/PostListPage";


function App() {
  return (
      <Routes>
        <Route path="/" element={<Navigate to="/posts" replace />} />
        <Route path="/posts" element={<PostListPage />} />
      </Routes>
  );
}

export default App;