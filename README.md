# 졸업작품
졸업작품을 만들면서 구현하면서 배운 내용 정리하고자 함

# 1. 인공지능 모델 학습

## 딥러닝 모델 : YOLOv8 학습

이번 졸업작품에서 YOLOv8 모델을 학습시켜야 함

YOLO : 객체 인식을 하는 모델로 이미지를 입력으로 받아 그 이미지 안에 무슨 사물(객체)가 있는지를 출력하는 모델

### YOLO 모델을 학습하는 2가지 방법
1. pyTorch기반 YOLO모델을 갖고 와서 파인튜닝하여 내 입맛대로 사용
2. Tensorflow기반의 YOLO모델을 내가 직접 데이터셋을 넣어서 학습

내가 선택한 방법 : 1번
#### 이유
1. 파인튜닝하여 사용하는 것이 모델의 정확도가 훨씬 좋음
2. 생배로 모델을 학습시키게 되면 처음하는거라 애초에 오류도 많은데 정확도가 낮은 모델을 만드는데에 1번의 방법보다 몇 배는 더 많은 시간이 소요됨

### pyTorch 기반의 YOLO 모델 파인튜닝

준비물
1. YOLOv8 모델
2. 내가 만들려고 하는 작품의 데이터셋(현재의 경우에는 음식 재료 데이터셋)
3. Google colab

***

# 2025/05/19
## 현재까지 마주한 문제점
YOLO모델은 이미지의 데이터셋을 넣어준다고 학습을 하는 게 아님

데이터셋에 대한 문제도 생김

데이터셋이 어떤 모양을 갖고 있느냐에 따라 YOLO모델이 2가지로 나뉨


### 1. data.yaml??

'data.yaml' : 데이터셋과 함께 작성해서 추가해줘야하는 파일로, 이 데이터셋이 어떤 데이터셋인지 알려주는 일종의 '설명서' 역할임

형식 : 다음과 같다

```
path: /content/drive/MyDrive/yolov8_datasets/
train: images/train
val: images/valid
test: images/test 

nc: 78
names:
  - 사과
  - 아스파라거스
  - 아보카도
  - 베이컨
  - 대나무순
  - 바나나
  - 강낭콩
  - 소고기
  - 비트
  - 검은콩
  - 빵
  - 가지
  - 광저콩
  - 브로콜리
  - 버터
  - 양배추
  - 피망
  - 당근
  - 콜리플라워
  - 치즈
  - 닭똥집
  - 닭고기
  - 병아리콩
  - 고추
  - 고춧가루
  - 볶음면
  - 계피
  - 고수
  - 옥수수
  - 콘플레이크
  - 호박순
  - 오이
  - 달걀
  - 호박잎
  - 고사리
  - 생선
  - 완두콩
  - 나물
  - 마늘
  - 생강
  - 그린피스
  - 풋콩
  - 햄
  - 얼음
  - 케첩
  - 레몬
  - 라임
  - 우유
  - 다진 고기
  - 버섯
  - 양고기
  - 올리브 오일
  - 쪽파
  - 양파
  - 오렌지
  - 완두
  - 배
  - 돼지고기
  - 감자
  - 호박
  - 무
  - 갓나물
  - 붉은 렌즈콩
  - 쌀
  - 딸기
  - 설탕
  - 고구마
  - 토란줄기
  - 두부
  - 토마토
  - 순무
  - 호두
  - 수박
  - 밀가루
  - 김치
  - 마요네즈
  - 국수
  - 김

```

path : 데이터셋의 위치

trani : 훈련셋

vaild : 검증셋

test : 테스트셋

nc : 클래스 갯수(데이테셋에 있는 객체들의 갯수)

names : 각각의 클래스의 이름(데이터셋에 있는 이미지의 이름 --> 재료들의 이름)

----

### 2. 데이터셋에 대한 문제

데이터셋 : 해외의 냉장고 재료 사진 약 1500장

'해외' --> 한국의 흔한 가정에서 사용하지 않는 재료들도 많음

해결법 : 데이터 전처리 과정이 있어야 함.

데이터 전처리 과정의 코드

```

import os

# 제거할 클래스 인덱스 (총 24개)
removed_ids = {
    0, 2, 3, 10, 13, 14, 15, 17, 22, 27, 29,
    46, 50, 54, 56, 58, 60, 63, 64, 70, 72,
    76, 82
}

# 병합할 클래스 (75 → 86)
merge_map = {75: 86}

# 원본 전체 인덱스
original_indices = list(range(120))

# index_map 자동 생성
index_map = {}
new_idx = 0
for i in original_indices:
    if i in removed_ids or i in merge_map:
        continue
    index_map[i] = new_idx
    new_idx += 1

# 병합된 인덱스도 매핑
for src, dst in merge_map.items():
    index_map[src] = index_map[dst]

# ✅ 디버깅 출력 (선택)
print(f"최종 클래스 수: {len(set(index_map.values()))}")
# print(index_map)  # 원하면 출력 확인

# 라벨 파일 재매핑 함수
def remap_labels(label_dir):
    for filename in os.listdir(label_dir):
        if not filename.endswith(".txt"):
            continue
        filepath = os.path.join(label_dir, filename)
        new_lines = []
        with open(filepath, "r") as f:
            for line in f:
                parts = line.strip().split()
                if not parts:
                    continue
                cls = int(parts[0])
                if cls in removed_ids:
                    continue
                new_cls = index_map.get(cls)
                if new_cls is None:
                    continue
                parts[0] = str(new_cls)
                new_lines.append(" ".join(parts))
        with open(filepath, "w") as f:
            f.write("\n".join(new_lines) + "\n")

# 사용 예시
remap_labels("/content/drive/MyDrive/yolov8_datasets/labels/train")
remap_labels("/content/drive/MyDrive/yolov8_datasets/labels/valid")
# remap_labels("/content/dataset/labels/test")  # 필요 시

```

제거할 클래스 인덱스 : 내가 직접 재료들을 보고 그 재료들을 ChatGpt에게 질문을 해서 분별해냈음

결과 : 총 120개의 클래스 중 78개의 클래스로 줄여버림

----

### 3. 데이터셋이 어떤 모양을 갖고 있느냐에 따라 YOLO모델이 2가지로 나뉨

데이터셋의 이미지가 세그맨테이션 형식이냐  검출용(detection) 형식이냐에 따라 다른 모델을 사용해줘야함

#### 형식의 종류
1. 세그맨테이션 형식
    
파일의 포맷이 5개 이상 나오는 형식

마스크 추가 처리가 필요함
   
polygon 좌표가 필요함 (x1, x2, x3 ...)

사용 모델 : yolov8n-seg.pt

학습 시 task='segment'로 지정해줘야 함

---
   
2. 검출용(detection) 형식

파일의 포맷이 5개만 나오는 형식

bounding box만 들어 있음

사용 모델 : yolov8n.pt

   학습 시 : task='detect'로 지정해줘야 함
