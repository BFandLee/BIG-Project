// src/data/mockPhotos.js

// 1. assets 폴더에서 사용할 이미지들을 import 합니다.
import photo1 from "../assets/pic1.jpg";
import photo2 from "../assets/pic2.jpeg";
import photo3 from "../assets/pic3.jpeg";
import photo4 from "../assets/pic4.jpg";
import photo5 from "../assets/pic5.jpg";
import photo6 from "../assets/pic6.jpg";
import photo7 from "../assets/pic7.jpg";
import photo8 from "../assets/pic8.jpg";
import photo9 from "../assets/pic9.jpg";
// ... 필요한 만큼 더 가져옵니다.

// 2. import한 이미지 변수를 imageUrl 값으로 사용합니다.
const mockPhotos = [
  { id: 1, imageUrl: photo1, title: "삼겹 수육", category: "korean" },
  { id: 2, imageUrl: photo2, title: "삼겹살 볶음", category: "korean" },
  { id: 3, imageUrl: photo3, title: "매운삼겹볶음", category: "chinese" },
  { id: 4, imageUrl: photo4, title: "오징어 볶음", category: "chinese" },
  { id: 5, imageUrl: photo5, title: "떡볶이", category: "korean" },
  { id: 6, imageUrl: photo6, title: "콩나물 국밥", category: "japanese" },
  { id: 7, imageUrl: photo7, title: "맛있는 김치전^^", category: "korean" },
  { id: 8, imageUrl: photo8, title: "된장찌개", category: "japanese" },
  { id: 9, imageUrl: photo9, title: "오징어 김치 완자", category: "korean" },
  // ...
];

export default mockPhotos;
