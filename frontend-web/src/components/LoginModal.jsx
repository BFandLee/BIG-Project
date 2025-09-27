import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import "./LoginModal.css";

const LoginModal = ({ isOpen, onClose, onSwitchToSignUp }) => {
  const { login } = useAuth();
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await login({ userId, password });
      onClose();
    } catch (err) {
      setError(err.response?.data || "로그인에 실패했습니다.");
    }
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <h2>로그인</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              placeholder="아이디"
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          {error && (
            <p
              className="error-message"
              style={{ color: "red", fontSize: "0.9em" }}
            >
              {error}
            </p>
          )}
          <button type="submit" className="login-btn">
            로그인
          </button>
        </form>
        <button onClick={onClose} className="close-btn">
          닫기
        </button>
        <div className="modal-options">
          <button className="option-button" onClick={onSwitchToSignUp}>
            회원가입
          </button>
          <span className="separator">|</span>
          <button className="option-button">아이디/비밀번호 찾기</button>
        </div>
      </div>
    </div>
  );
};

export default LoginModal;
