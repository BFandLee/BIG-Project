# server.py
from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from ultralytics import YOLO
from PIL import Image
import io

# ===== 설정 =====
WEIGHTS_PATH = "C:/Program Files/BIG-Project/yolo8_results/exp19/weights/best.pt"  # 너의 가중치 경로
CONF_TH = 0.60
TARGET_CLASSES = {"egg", "meat"}  # 시연용 필터 (원하면 비워두면 전체 허용)

app = FastAPI(title="FridgeAI - YOLO Inference API")

# 앱 시작 시 1회 로드
@app.on_event("startup")
def load_model():
    global model
    model = YOLO(WEIGHTS_PATH)   # CUDA 있으면 자동 사용, 강제시 model.to('cuda')

@app.get("/health")
def health():
    return {"ok": True}

class DetectItem(BaseModel):
    name: str
    confidence: float
    bbox: list[int]

class DetectResp(BaseModel):
    ingredients: list[str]
    detections: list[DetectItem]

@app.post("/detect", response_model=DetectResp)
async def detect(file: UploadFile = File(...)):
    try:
        img_bytes = await file.read()
        img = Image.open(io.BytesIO(img_bytes)).convert("RGB")

        # YOLO 추론
        res = model(img, verbose=False)[0]

        items = []
        for box in res.boxes:
            cls_id = int(box.cls.item())
            name = res.names[cls_id]
            conf = float(box.conf.item())
            if (not TARGET_CLASSES or name in TARGET_CLASSES) and conf >= CONF_TH:
                x1, y1, x2, y2 = map(int, box.xyxy[0].tolist())
                items.append(DetectItem(
                    name=name,
                    confidence=round(conf, 2),
                    bbox=[x1, y1, x2, y2]
                ))

        ingredients = sorted({it.name for it in items})
        return DetectResp(ingredients=ingredients, detections=items)

    except Exception as e:
        raise HTTPException(status_code=400, detail=f"invalid image or inference error: {e}")
