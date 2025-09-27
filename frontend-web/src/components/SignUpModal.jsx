import React, { useState } from "react";
import "./SignUpModal.css";
import { signUp } from "../services/apiService";

const SignUpModal = ({ isOpen, onClose, onSignUpSuccess }) => {
  const [formData, setFormData] = useState({
    userId: "",
    password: "",
    name: "",
    phone: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await signUp(formData);
      alert("회원가입 성공!");
      onSignUpSuccess();
    } catch (err) {
      alert(err.response?.data || "회원가입에 실패했습니다.");
      console.error(err);
    }
  };

  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <h2>회원가입</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              name="userId"
              placeholder="아이디"
              value={formData.userId}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="password"
              name="password"
              placeholder="비밀번호"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              name="name"
              placeholder="이름"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="tel"
              name="phone"
              placeholder="전화번호 ex) 010-0000-0000"
              value={formData.phone}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" className="signup-btn">
            가입하기
          </button>
        </form>
        <button onClick={onClose} className="close-btn">
          닫기
        </button>
      </div>
    </div>
  );
};

export default SignUpModal;
