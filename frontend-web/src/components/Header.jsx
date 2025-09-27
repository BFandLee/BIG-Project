import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "./Header.css";

const Header = ({ onMenuClick, onLoginClick, onSearchChange, searchTerm }) => {
  const { loginUser, logout } = useAuth();

  return (
    <div className="header-body">
      <div className="header-layout">
        <div className="header-top">
          <div className="header-right">
            {loginUser ? (
              <div className="user-profile">
                <span>👤 {loginUser.name} 님</span>
                <button className="logout-button" onClick={logout}>
                  로그아웃
                </button>
              </div>
            ) : (
              <button className="login-button-header" onClick={onLoginClick}>
                로그인
              </button>
            )}
          </div>
        </div>
        <div className="header-bottom">
          <div className="header-left">
            <button className="menu-button" onClick={onMenuClick}>
              <svg
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M4 6H20M4 12H20M4 18H20"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </button>
          </div>
          <div className="header-center">
            <div className="search-bar">
              <span className="search-icon">
                <svg
                  width="20"
                  height="20"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M11 19C15.4183 19 19 15.4183 19 11C19 6.58172 15.4183 3 11 3C6.58172 3 3 6.58172 3 11C3 15.4183 6.58172 19 11 19ZM21 21L16.65 16.65"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
              </span>
              <input
                type="text"
                placeholder="요리를 검색해보세요"
                value={searchTerm}
                onChange={(e) => onSearchChange(e.target.value)}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Header;
