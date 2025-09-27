import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./MainPage.css";
import { getPosts } from "../services/apiService";
import { categories as importedCategories } from "../data/categories";

const categories = [{ key: "all", name: "전체" }, ...importedCategories];

const PHOTOS_PER_PAGE = 9;

const MainPage = ({ searchTerm }) => {
  const [currentPage, setCurrentPage] = useState(1);
  const [activeCategory, setActiveCategory] = useState("all");
  const [posts, setPosts] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        setLoading(true);
        setError(null);
        const response = await getPosts(
          activeCategory,
          currentPage - 1,
          PHOTOS_PER_PAGE,
          searchTerm
        );
        setPosts(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
      } catch (err) {
        setError("게시글을 불러오는 데 실패했습니다.");
        setPosts([]);
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchPosts();
  }, [activeCategory, currentPage, searchTerm]);

  const handleCategoryClick = (categoryKey) => {
    setActiveCategory(categoryKey);
    setCurrentPage(1);
  };

  const handlePrevPage = () => {
    if (currentPage > 1) setCurrentPage(currentPage - 1);
  };
  const handleNextPage = () => {
    if (currentPage < totalPages) setCurrentPage(currentPage + 1);
  };

  if (loading) return <div></div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="main-container">
      <div className="category-menu">
        {categories.map((cat) => (
          <button
            key={cat.key}
            className={`category-item ${
              activeCategory === cat.key ? "active" : ""
            }`}
            onClick={() => handleCategoryClick(cat.key)}
          >
            {cat.name}
          </button>
        ))}
      </div>

      <div className="photo-grid">
        {posts.map((post) => (
          <div key={post.id} className="photo-card">
            <Link to={`/photo/${post.id}`}>
              <img src={post.imageUrl} alt={post.title} />
            </Link>
            <div className="photo-title">{post.title}</div>
          </div>
        ))}
      </div>

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
            className={`page-button ${currentPage === number ? "active" : ""}`}
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
    </div>
  );
};

export default MainPage;
