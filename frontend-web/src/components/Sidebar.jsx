import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "./Sidebar.css";

const Sidebar = ({ isOpen, onClose }) => {
  const { loginUser } = useAuth();

  return (
    <>
      <div
        className={`sidebar-overlay ${isOpen ? "open" : ""}`}
        onClick={onClose}
      ></div>
      <div className={`sidebar-container ${isOpen ? "open" : ""}`}>
        <nav className="sidebar-nav top-menu">
          <Link to="/" className="sidebar-link" onClick={onClose}>
            메인화면
          </Link>
          <Link to="/suggestions" className="sidebar-link" onClick={onClose}>
            건의사항
          </Link>
          <Link to="/ask-gpt" className="sidebar-link" onClick={onClose}>
            GPT에게 물어보기
          </Link>
        </nav>

        {loginUser && (
          <nav className="sidebar-nav bottom-menu">
            <Link to="/create-post" className="sidebar-link" onClick={onClose}>
              요리 업로드 🍳
            </Link>
            <Link to="/my-posts" className="sidebar-link" onClick={onClose}>
              My History 🍳
            </Link>
          </nav>
        )}
      </div>
    </>
  );
};

export default Sidebar;
