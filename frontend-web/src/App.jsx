import React, { useState } from "react";
import { Routes, Route, Link } from "react-router-dom";
import Header from "./components/Header";
import Sidebar from "./components/Sidebar";
import LoginModal from "./components/LoginModal";
import SignUpModal from "./components/SignUpModal";
import MainPage from "./pages/MainPage";
import PhotoDetailPage from "./pages/PhotoDetailPage";
import SuggestionsPage from "./pages/SuggestionsPage";
import AskGptPage from "./pages/AskGptPage";
import CreatePostPage from "./pages/CreatePostPage";
import EditPostPage from "./pages/EditPostPage";
import MyPostsPage from "./pages/MyPostsPage";
import "./App.css";

function App() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isLoginModalOpen, setIsLoginModalOpen] = useState(false);
  const [isSignUpModalOpen, setIsSignUpModalOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  const switchToSignUp = () => {
    setIsLoginModalOpen(false);
    setIsSignUpModalOpen(true);
  };

  const handleSignUpSuccess = () => {
    setIsSignUpModalOpen(false);
    setIsLoginModalOpen(true);
  };

  return (
    <div className="App">
      <Sidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
      <LoginModal
        isOpen={isLoginModalOpen}
        onClose={() => setIsLoginModalOpen(false)}
        onSwitchToSignUp={switchToSignUp}
      />
      <SignUpModal
        isOpen={isSignUpModalOpen}
        onClose={() => setIsSignUpModalOpen(false)}
        onSignUpSuccess={handleSignUpSuccess}
      />

      <a href="/" className="site-title-link">
        <h1 className="site-title">냉장고를 열어봐 🍳</h1>
      </a>

      <Header
        onMenuClick={toggleSidebar}
        onLoginClick={() => setIsLoginModalOpen(true)}
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
      />

      <main>
        <Routes>
          <Route
            path="/"
            element={
              <MainPage searchTerm={searchTerm} setSearchTerm={setSearchTerm} />
            }
          />
          <Route path="/photo/:postId" element={<PhotoDetailPage />} />
          <Route path="/create-post" element={<CreatePostPage />} />
          <Route path="/edit-post/:postId" element={<EditPostPage />} />
          <Route path="/suggestions" element={<SuggestionsPage />} />
          <Route path="/ask-gpt" element={<AskGptPage />} />
          <Route path="/my-posts" element={<MyPostsPage />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
