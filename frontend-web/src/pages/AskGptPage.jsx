import React, { useState } from "react";
import { askGpt } from "../services/apiService";
import "./AskGptPage.css";

const AskGptPage = () => {
  const [prompt, setPrompt] = useState("");
  const [answer, setAnswer] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (prompt.trim() === "") return;

    setIsLoading(true);
    setAnswer("");
    try {
      const response = await askGpt(prompt);
      setAnswer(response.data.answer);
    } catch (error) {
      setAnswer("오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="gpt-container">
      <h1>GPT에게 물어봐</h1>
      <p className="gpt-description">
        냉장고 속 재료로 만들 수 있는 요리를 gpt에게 물어보세요
      </p>

      <form onSubmit={handleSubmit} className="gpt-form">
        <textarea
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
          placeholder="예: 냉장고에 계란, 대파, 밥이 있어요. 뭘 만들 수 있을까요?"
          rows="4"
          disabled={isLoading}
        />
        <button type="submit" disabled={isLoading}>
          {isLoading ? "생각 중..." : "물어보기"}
        </button>
      </form>

      {isLoading && <div className="loading-spinner"></div>}

      {answer && (
        <div className="gpt-answer">
          <h2>GPT의 답변:</h2>
          <pre>{answer}</pre>
        </div>
      )}
    </div>
  );
};

export default AskGptPage;
