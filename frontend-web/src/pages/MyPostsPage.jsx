import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "./MainPage.css";
import { getMyPosts } from "../services/apiService";

const POSTS_PER_PAGE = 9;

const MyPostsPage = () => {
  const { loginUser } = useAuth();
  const navigate = useNavigate();

  const [posts, setPosts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!loginUser) {
      alert("로그인이 필요한 페이지입니다.");
      navigate("/"); // 로그인 정보가 없으면 메인 페이지로 이동
      return;
    }

    const fetchMyPosts = async () => {
      setLoading(true);
      try {
        setError(null);
        const response = await getMyPosts(
          loginUser.id,
          currentPage - 1,
          POSTS_PER_PAGE
        );
        setPosts(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
      } catch (err) {
        setError("게시글을 불러오는 데 실패했습니다.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchMyPosts();
  }, [currentPage, loginUser, navigate]);

  const handlePrevPage = () => {
    if (currentPage > 1) setCurrentPage(currentPage - 1);
  };
  const handleNextPage = () => {
    if (currentPage < totalPages) setCurrentPage(currentPage + 1);
  };

  if (!loginUser) {
    // useEffect에서 navigate가 실행되기 전 잠시 렌더링되는 것을 방지
    return null;
  }

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="main-container">
      <h1 className="page-title">My History 🍳</h1>
      <div className="photo-grid">
        {posts.length > 0 ? (
          posts.map((post) => (
            <div key={post.id} className="photo-card">
              <Link to={`/photo/${post.id}`}>
                <img
                  src={`http://localhost:8080${post.imageUrl}`}
                  alt={post.title}
                />
              </Link>
              <div className="photo-title">{post.title}</div>
            </div>
          ))
        ) : (
          <div className="no-posts-message">작성한 게시글이 없습니다.</div>
        )}
      </div>

      {totalPages > 1 && (
        <div className="pagination">
          <button
            className="page-button arrow-button"
            onClick={handlePrevPage}
            disabled={currentPage === 1}
          >
            &lt;
          </button>
          {Array.from({ length: totalPages }, (_, i) => i + 1).map((number) => (
            <button
              key={number}
              className={`page-button ${
                currentPage === number ? "active" : ""
              }`}
              onClick={() => setCurrentPage(number)}
            >
              {number}
            </button>
          ))}
          <button
            className="page-button arrow-button"
            onClick={handleNextPage}
            disabled={currentPage === totalPages}
          >
            &gt;
          </button>
        </div>
      )}
    </div>
  );
};

export default MyPostsPage;
