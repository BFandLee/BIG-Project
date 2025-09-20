package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 냉장고 문 상태를 보관/토글하는 ViewModel.
 * 기존 MainActivity.kt의 top/middle 상태를 분리함. :contentReference[oaicite:1]{index=1}
 */
class FridgeViewModel : ViewModel() {

    // 상단/중단 문 열림 여부 (기본: 닫힘=false)
    val topDoorOpen: MutableLiveData<Boolean> = MutableLiveData(false)
    val middleDoorOpen: MutableLiveData<Boolean> = MutableLiveData(false)

    fun toggleTop() {
        val now = topDoorOpen.value ?: false
        topDoorOpen.value = !now
    }

    fun toggleMiddle() {
        val now = middleDoorOpen.value ?: false
        middleDoorOpen.value = !now
    }
}