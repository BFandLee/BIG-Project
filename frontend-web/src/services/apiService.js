import axios from "axios";

// API 요청을 위한 기본 axios 인스턴스 설정
// Vite의 proxy 설정을 사용하므로 baseURL은 'http://localhost:8080'을 명시할 필요가 없습니다.
const apiClient = axios.create({
  headers: {
    "Content-Type": "application/json",
  },
});

// ===================================
// 회원 인증 (Auth) API
// ===================================

/**
 * 회원가입을 요청합니다.
 * @param {object} userData - { userId, password, name, phone }
 */
export const signUp = (userData) => {
  return apiClient.post("/api/auth/signup", userData);
};

/**
 * 로그인을 요청합니다.
 * @param {object} credentials - { userId, password }
 */
export const login = (credentials) => {
  return apiClient.post("/api/auth/login", credentials);
};

// ===================================
// 게시글 (Post) API
// ===================================

/**
 * 게시글 목록을 조회합니다. (카테고리, 페이지, 검색어, 현재 유저 ID)
 * @param {string} category - 'all', 'korean' 등
 * @param {number} page - 페이지 번호 (0부터 시작)
 * @param {number} size - 페이지당 게시글 수
 * @param {string} search - 검색어
 * @param {number|null} currentUserId - 현재 로그인한 사용자의 ID (좋아요 여부 확인용)
 */
export const getPosts = (
  category,
  page = 0,
  size = 9,
  search = "",
  currentUserId = null
) => {
  return apiClient.get("/api/posts", {
    params: { category, page, size, search, currentUserId },
  });
};

/**
 * ID로 특정 게시글의 상세 정보를 조회합니다.
 * @param {number} postId - 게시글 ID
 * @param {number|null} currentUserId - 현재 로그인한 사용자의 ID (좋아요 여부 확인용)
 */
export const getPostById = (postId, currentUserId = null) => {
  return apiClient.get(`/api/posts/${postId}`, { params: { currentUserId } });
};

/**
 * 새 게시글을 작성합니다. (파일 포함)
 * @param {object} postData - { title, content, category, ingredients, userId }
 * @param {File} imageFile - 업로드할 이미지 파일
 */
export const createPost = (postData, imageFile) => {
  const formData = new FormData();
  // JSON 데이터는 Blob 형태로 변환하여 'postData'라는 이름으로 추가합니다.
  formData.append(
    "postData",
    new Blob([JSON.stringify(postData)], { type: "application/json" })
  );
  // 이미지 파일은 'imageFile'이라는 이름으로 추가합니다.
  formData.append("imageFile", imageFile);

  return apiClient.post("/api/posts", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

/**
 * 게시글을 수정합니다. (파일 포함 가능)
 * @param {number} postId - 수정할 게시글 ID
 * @param {object} postData - { title, content, category, ingredients, userId }
 * @param {File|null} imageFile - 새로 업로드할 이미지 파일 (선택 사항)
 */
export const updatePost = (postId, postData, imageFile) => {
  const formData = new FormData();
  formData.append(
    "postData",
    new Blob([JSON.stringify(postData)], { type: "application/json" })
  );
  if (imageFile) {
    formData.append("imageFile", imageFile);
  }

  return apiClient.put(`/api/posts/${postId}`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

/**
 * 게시글을 삭제합니다.
 * @param {number} postId - 삭제할 게시글 ID
 * @param {number} userId - 삭제를 요청하는 사용자 ID (권한 확인용)
 */
export const deletePost = (postId, userId) => {
  return apiClient.delete(`/api/posts/${postId}`, { params: { userId } });
};

/**
 * 게시글 좋아요 상태를 변경(토글)합니다.
 * @param {number} postId - 좋아요를 누를 게시글 ID
 * @param {number} userId - 좋아요를 누르는 사용자 ID
 */
export const toggleLikePost = (postId, userId) => {
  return apiClient.post(`/api/posts/${postId}/toggle-like`, null, {
    params: { userId },
  });
};

/**
 * 특정 사용자가 작성한 게시글 목록을 조회합니다.
 * @param {number} userId - 게시글을 조회할 사용자 ID
 * @param {number} page - 페이지 번호
 * @param {number} size - 페이지당 게시글 수
 */
export const getMyPosts = (userId, page = 0, size = 9) => {
  return apiClient.get("/api/posts/my-posts", {
    params: { userId, page, size },
  });
};

// ===================================
// 댓글 (Comment) API
// ===================================

/**
 * 특정 게시글의 댓글 목록을 조회합니다.
 * @param {number} postId - 댓글을 조회할 게시글 ID
 */
export const getComments = (postId) => {
  return apiClient.get(`/api/posts/${postId}/comments`);
};

/**
 * 새 댓글을 작성합니다.
 * @param {number} postId - 댓글을 작성할 게시글 ID
 * @param {object} commentData - { content, userId, parentId }
 */
export const createComment = (postId, commentData) => {
  return apiClient.post(`/api/posts/${postId}/comments`, commentData);
};

/**
 * 댓글을 수정합니다.
 * @param {number} commentId - 수정할 댓글 ID
 * @param {string} content - 새로운 댓글 내용
 * @param {number} userId - 수정을 요청하는 사용자 ID
 */
export const updateComment = (commentId, content, userId) => {
  return apiClient.put(
    `/api/comments/${commentId}`,
    { content },
    { params: { userId } }
  );
};

/**
 * 댓글을 삭제합니다.
 * @param {number} commentId - 삭제할 댓글 ID
 * @param {number} userId - 삭제를 요청하는 사용자 ID
 */
export const deleteComment = (commentId, userId) => {
  return apiClient.delete(`/api/comments/${commentId}`, { params: { userId } });
};

// ===================================
// GPT API
// ===================================

/**
 * GPT에게 질문을 보냅니다.
 * @param {string} prompt - GPT에게 보낼 질문
 */
export const askGpt = (prompt) => {
  return apiClient.post("/api/gpt/ask", { prompt });
};
