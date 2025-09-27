import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getPostById, updatePost } from "../services/apiService";
import { categories } from "../data/categories";
import { useAuth } from "../context/AuthContext";
import "./CreatePostPage.css"; // 글 작성 페이지와 동일한 스타일 사용

const EditPostPage = () => {
  const { postId } = useParams();
  const navigate = useNavigate();
  const { loginUser } = useAuth();

  const [formData, setFormData] = useState({
    title: "",
    content: "",
    category: "korean",
    ingredients: "",
  });
  const [currentImageUrl, setCurrentImageUrl] = useState(""); // 기존 이미지 URL
  const [newImageFile, setNewImageFile] = useState(null); // 새로 선택한 이미지 파일
  const [newImagePreviewUrl, setNewImagePreviewUrl] = useState(""); // 새 이미지 미리보기 URL
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPostData = async () => {
      if (!loginUser) {
        alert("로그인이 필요합니다.");
        navigate("/");
        return;
      }
      try {
        const response = await getPostById(postId, loginUser.id);
        const { title, content, category, ingredients, imageUrl, authorId } =
          response.data;

        // 본인 확인
        if (authorId !== loginUser.id) {
          alert("수정할 권한이 없습니다.");
          navigate(`/photo/${postId}`);
          return;
        }

        setFormData({ title, content, category, ingredients });
        setCurrentImageUrl(imageUrl);
      } catch (err) {
        setError("게시글 정보를 불러오는 데 실패했습니다.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchPostData();
  }, [postId, loginUser, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({ ...prevState, [name]: value }));
  };

  const handleNewImageChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setNewImageFile(file);
      if (newImagePreviewUrl) URL.revokeObjectURL(newImagePreviewUrl);
      setNewImagePreviewUrl(URL.createObjectURL(file));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const postUpdateData = { ...formData, userId: loginUser.id };

    try {
      await updatePost(postId, postUpdateData, newImageFile);
      alert("게시글이 수정되었습니다.");
      navigate(`/photo/${postId}`);
    } catch (err) {
      alert(err.response?.data || "수정에 실패했습니다.");
    }
  };

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="create-post-container">
      <h1>게시글 수정하기</h1>
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
                  checked={formData.category === cat.key}
                  onChange={handleChange}
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
            name="title"
            type="text"
            value={formData.title}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="ingredients">사용한 재료</label>
          <input
            id="ingredients"
            name="ingredients"
            type="text"
            value={formData.ingredients}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="image">사진 변경 (선택)</label>
          <input
            id="image"
            type="file"
            accept="image/*"
            onChange={handleNewImageChange}
          />
          <div className="image-previews">
            {!newImagePreviewUrl && currentImageUrl && (
              <div className="preview-item">
                <p>현재 사진:</p>
                <img
                  src={`http://localhost:8080${currentImageUrl}`}
                  alt="현재 이미지"
                  className="image-preview"
                />
              </div>
            )}
            {newImagePreviewUrl && (
              <div className="preview-item">
                <p>새로운 사진 미리보기:</p>
                <img
                  src={newImagePreviewUrl}
                  alt="새로운 이미지 미리보기"
                  className="image-preview"
                />
              </div>
            )}
          </div>
        </div>
        <div className="form-group">
          <label htmlFor="content">내용</label>
          <textarea
            id="content"
            name="content"
            value={formData.content}
            onChange={handleChange}
            rows="10"
            required
          ></textarea>
        </div>
        <button type="submit" className="submit-btn">
          수정 완료
        </button>
      </form>
    </div>
  );
};

export default EditPostPage;
