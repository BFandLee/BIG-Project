import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { createPost } from "../services/apiService";
import { categories } from "../data/categories";
import "./CreatePostPage.css";

const CreatePostPage = () => {
  const { loginUser } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!loginUser) {
      alert("로그인이 필요한 페이지입니다.");
      navigate("/");
    }
  }, [loginUser, navigate]);

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [imageUrl, setImageUrl] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("korean");
  const [ingredients, setIngredients] = useState("");

  const handleImageChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setImageFile(file);
      if (imageUrl) URL.revokeObjectURL(imageUrl);
      setImageUrl(URL.createObjectURL(file));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!imageFile) {
      alert("사진을 첨부해주세요.");
      return;
    }

    const postData = {
      title,
      content,
      category: selectedCategory,
      ingredients,
      userId: loginUser.id,
    };

    try {
      const response = await createPost(postData, imageFile);
      alert("게시글이 등록되었습니다.");
      navigate(`/photo/${response.data.id}`);
    } catch (err) {
      const errorMessage = err.response?.data || "게시글 등록에 실패했습니다.";
      alert(errorMessage);
    }
  };

  if (!loginUser) return null; // 로그인 상태가 아닐 경우 렌더링하지 않음

  return (
    <div className="create-post-container">
      <h1>새 글 작성하기</h1>
      <form onSubmit={handleSubmit} className="post-form">
        <div className="form-group">
          <label>카테고리</label>
          <div className="category-select">
            {categories.map((cat) => (
              <label key={cat.key} className="radio-label">
                <input
                  type="radio"
                  name="category"
                  value={cat.key}
                  checked={selectedCategory === cat.key}
                  onChange={(e) => setSelectedCategory(e.target.value)}
                />
                <span>{cat.name}</span>
              </label>
            ))}
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="title">제목</label>
          <input
            id="title"
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="제목을 입력하세요"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="ingredients">사용한 재료</label>
          <input
            id="ingredients"
            type="text"
            value={ingredients}
            onChange={(e) => setIngredients(e.target.value)}
            placeholder="예: 돼지고기, 김치, 두부, 대파"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="image">사진 첨부</label>
          <input
            id="image"
            type="file"
            accept="image/*"
            onChange={handleImageChange}
            required
          />
        </div>
        {imageUrl && (
          <img src={imageUrl} alt="미리보기" className="image-preview" />
        )}
        <div className="form-group">
          <label htmlFor="content">내용</label>
          <textarea
            id="content"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="내용을 입력하세요"
            rows="10"
            required
          ></textarea>
        </div>
        <button type="submit" className="submit-btn">
          등록하기
        </button>
      </form>
    </div>
  );
};

export default CreatePostPage;
