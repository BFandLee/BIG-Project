import initialPhotos from "./mockPhotos"; // 초기 데이터로 사용할 mockPhotos

const PHOTO_STORAGE_KEY = "photos";

// 앱 시작 시 데이터 초기화 로직
const initPhotos = () => {
  const photos = localStorage.getItem(PHOTO_STORAGE_KEY);
  if (!photos) {
    // localStorage에 사진 데이터가 없으면 mockPhotos로 초기화
    localStorage.setItem(PHOTO_STORAGE_KEY, JSON.stringify(initialPhotos));
  }
};

// 모든 사진 가져오기
const getAllPhotos = () => {
  const photos = localStorage.getItem(PHOTO_STORAGE_KEY);
  return photos ? JSON.parse(photos) : [];
};

// ID로 특정 사진 하나 가져오기
const getPhotoById = (id) => {
  const photos = getAllPhotos();
  return photos.find((p) => p.id === parseInt(id));
};

// 새로운 사진 추가하기
const addPhoto = (photoData) => {
  const photos = getAllPhotos();
  const newPhoto = {
    ...photoData,
    id: Date.now(), // 고유한 ID로 현재 시간을 사용
    // 실제로는 이미지를 서버에 업로드하고 URL을 받지만, 여기서는 임시 URL을 사용합니다.
    imageUrl:
      photoData.imageUrl ||
      "https://via.placeholder.com/400x300.png?text=New+Post",
  };
  photos.unshift(newPhoto); // 새 사진을 맨 앞에 추가
  localStorage.setItem(PHOTO_STORAGE_KEY, JSON.stringify(photos));
  return newPhoto; // 생성된 사진 정보 반환
};

// 서비스 함수들을 export
export const photoService = {
  init: initPhotos,
  getAllPhotos,
  getPhotoById,
  addPhoto,
};

// 앱이 시작될 때 딱 한 번 초기화 함수를 실행합니다.
photoService.init();
