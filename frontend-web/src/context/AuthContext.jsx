import React, { createContext, useState, useContext } from "react";
import { login as apiLogin } from "../services/apiService";

const AuthContext = createContext(null);

// 다른 컴포넌트에서 이 훅을 import하여 사용합니다.
export const useAuth = () => {
  return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
  const [loginUser, setLoginUser] = useState(() => {
    try {
      const savedUser = sessionStorage.getItem("loginUser");
      return savedUser ? JSON.parse(savedUser) : null;
    } catch (error) {
      console.error("저장된 유저 정보를 파싱하는데 실패했습니다.", error);
      return null;
    }
  });

  const login = async (credentials) => {
    try {
      const response = await apiLogin(credentials);
      const user = response.data;
      setLoginUser(user);
      sessionStorage.setItem("loginUser", JSON.stringify(user));
      return user;
    } catch (error) {
      console.error("로그인 실패:", error);
      throw error;
    }
  };

  const logout = () => {
    setLoginUser(null);
    sessionStorage.removeItem("loginUser");
  };

  const value = {
    loginUser,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
