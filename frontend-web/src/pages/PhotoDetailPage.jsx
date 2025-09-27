import React, { useState, useEffect } from "react";
import { useParams, Link as RouterLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import {
  getPostById,
  getComments,
  createComment,
  updateComment,
  deleteComment,
  deletePost,
  toggleLikePost,
} from "../services/apiService";
import { categories } from "../data/categories";
import { IoHeartOutline, IoHeart } from "react-icons/io5";
import "./PhotoDetailPage.css";

const PhotoDetailPage = () => {
  const { loginUser } = useAuth();
  const { postId } = useParams();
  const navigate = useNavigate();

  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const [editingComment, setEditingComment] = useState(null);

  useEffect(() => {
    const fetchAllData = async () => {
      if (!postId) {
        setLoading(false);
        setError("유효하지 않은 게시글 ID입니다.");
        return;
      }
      try {
        setLoading(true);
        setError(null);
        const [postRes, commentsRes] = await Promise.all([
          getPostById(postId, loginUser ? loginUser.id : null),
          getComments(postId),
        ]);
        setPost(postRes.data);
        setComments(commentsRes.data);
      } catch (error) {
        const errorMessage =
          error.response?.data || "데이터를 불러오는 데 실패했습니다.";
        setError(errorMessage);
        console.error("데이터 로딩 오류:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchAllData();
  }, [postId, loginUser]);

  const handleLikeClick = async () => {
    if (!loginUser) return alert("로그인이 필요합니다.");

    const originalPost = { ...post };
    const newPost = {
      ...post,
      likedByCurrentUser: !post.likedByCurrentUser,
      likeCount: post.likedByCurrentUser
        ? post.likeCount - 1
        : post.likeCount + 1,
    };
    setPost(newPost);

    try {
      await toggleLikePost(post.id, loginUser.id);
    } catch (error) {
      const errorMessage =
        error.response?.data || "좋아요 처리에 실패했습니다.";
      alert(errorMessage);
      console.error("좋아요 오류:", error); // 개발자용 에러 로그
      setPost(originalPost); // 실패 시 원래 상태로 복구
    }
  };

  const handleDeletePost = async () => {
    if (!loginUser) return alert("로그인이 필요합니다.");
    if (window.confirm("정말 이 게시글을 삭제하시겠습니까?")) {
      try {
        await deletePost(post.id, loginUser.id);
        alert("게시글이 삭제되었습니다.");
        navigate("/");
      } catch (err) {
        alert(err.response?.data || "삭제에 실패했습니다.");
      }
    }
  };

  const handleEditPost = () => navigate(`/edit-post/${post.id}`);

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!loginUser) return alert("로그인이 필요합니다.");
    if (newComment.trim() === "") return;

    const commentData = { content: newComment, userId: loginUser.id, postId };
    try {
      const response = await createComment(postId, commentData);
      setComments([...comments, response.data]);
      setNewComment("");
    } catch (error) {
      alert(error.response?.data || "댓글 등록에 실패했습니다.");
    }
  };

  const handleDeleteComment = async (commentId) => {
    if (!loginUser) return;
    if (window.confirm("댓글을 삭제하시겠습니까?")) {
      try {
        await deleteComment(commentId, loginUser.id);
        setComments(comments.filter((c) => c.id !== commentId));
      } catch (error) {
        alert(error.response?.data || "댓글 삭제에 실패했습니다.");
      }
    }
  };

  const handleUpdateComment = async (commentId, content) => {
    if (!loginUser) return;
    try {
      const response = await updateComment(commentId, content, loginUser.id);
      setComments(
        comments.map((c) => (c.id === commentId ? response.data : c))
      );
      setEditingComment(null);
    } catch (error) {
      alert(error.response?.data || "댓글 수정에 실패했습니다.");
    }
  };

  if (loading) return <div>로딩 중...</div>;
  if (error || !post) return <div>{error || "게시글을 찾을 수 없습니다."}</div>;

  const categoryName =
    categories.find((cat) => cat.key === post.category)?.name || post.category;

  return (
    <div className="detail-container">
      <div className="title-header">
        <h1 className="detail-title">{post.title}</h1>
        {post.authorUserId && (
          <p className="author-info">작성자: {post.authorUserId}</p>
        )}
      </div>

      <img
        src={`http://localhost:8080${post.imageUrl}`}
        alt={post.title}
        className="detail-image"
      />

      <div className="like-section">
        <button
          onClick={handleLikeClick}
          className="like-button"
          disabled={!loginUser}
        >
          {post.likedByCurrentUser ? (
            <IoHeart className="like-icon liked" />
          ) : (
            <IoHeartOutline className="like-icon" />
          )}
        </button>
        <span className="like-count">좋아요 {post.likeCount}개</span>
      </div>

      <div className="detail-info">
        <p>
          <strong>카테고리:</strong> {categoryName}
        </p>
        <p>
          <strong>사용한 재료:</strong> {post.ingredients}
        </p>
      </div>
      <p className="detail-description">{post.content}</p>

      {loginUser && post && loginUser.id === post.authorId && (
        <div className="post-actions">
          <button className="action-button edit" onClick={handleEditPost}>
            수정하기
          </button>
          <button className="action-button delete" onClick={handleDeletePost}>
            삭제하기
          </button>
        </div>
      )}

      <div className="comments-section">
        <h2 className="comments-title">댓글</h2>
        <form onSubmit={handleCommentSubmit} className="comment-form">
          <input
            type="text"
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            placeholder={
              loginUser
                ? "댓글을 입력하세요..."
                : "로그인 후 댓글을 작성할 수 있습니다."
            }
            className="comment-input"
            disabled={!loginUser}
          />
          <button
            type="submit"
            className="comment-submit-btn"
            disabled={!loginUser}
          >
            등록
          </button>
        </form>

        <ul className="comments-list">
          {comments.map((comment) => (
            <li key={comment.id} className="comment-item">
              {editingComment?.id === comment.id ? (
                <form
                  className="edit-comment-form"
                  onSubmit={(e) => {
                    e.preventDefault();
                    handleUpdateComment(comment.id, editingComment.content);
                  }}
                >
                  <input
                    type="text"
                    value={editingComment.content}
                    onChange={(e) =>
                      setEditingComment({
                        ...editingComment,
                        content: e.target.value,
                      })
                    }
                    autoFocus
                  />
                  <button type="submit" className="edit-form-btn submit">
                    완료
                  </button>
                  <button
                    type="button"
                    className="edit-form-btn cancel"
                    onClick={() => setEditingComment(null)}
                  >
                    취소
                  </button>
                </form>
              ) : (
                <div className="comment-display">
                  <span className="comment-text">{comment.content}</span>
                  <div className="comment-meta">
                    <span className="comment-author">
                      {comment.authorUserId}
                    </span>
                    {loginUser && loginUser.id === comment.authorId && (
                      <div className="comment-actions">
                        <button
                          className="comment-action-btn edit"
                          onClick={() =>
                            setEditingComment({
                              id: comment.id,
                              content: comment.content,
                            })
                          }
                        >
                          수정
                        </button>
                        <button
                          className="comment-action-btn delete"
                          onClick={() => handleDeleteComment(comment.id)}
                        >
                          삭제
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              )}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default PhotoDetailPage;
