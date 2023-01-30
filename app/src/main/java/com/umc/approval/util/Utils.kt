package com.umc.approval.util

object Utils {

    /*camera*/
    val PICK_IMAGE_FROM_GALLERY = 1000
    val PICK_IMAGE_FROM_GALLERY_PERMISSION = 1010

    /*category*/
    val categoryMap = mapOf<Int, String>(0 to "디지털기기", 1 to "생활가전", 2 to "생활용품", 3 to "가구/인테리어", 4 to "주방/건강",
        5 to "출산/유아동", 6 to "패션의류/잡화", 7 to "뷰티/미용", 8 to "스포츠/헬스/레저", 9 to "취미/게임/완구", 10 to "문구/오피스",
        11 to "도서/음악", 12 to "티켓/교환권", 13 to "식품", 14 to "동물/식물", 15 to "영화/공연", 16 to "자동차/공구", 17 to "기타 물품")

    /*category*/
    val categoryMapReverse = mapOf<String, Int>("디지털기기" to 0,"생활가전" to 1,"생활용품" to 2,"가구/인테리어" to 3,"주방/건강" to 4,
        "출산/유아동" to 5, "패션의류/잡화" to 6, "뷰티/미용" to 7, "스포츠/헬스/레저" to 8, "취미/게임/완구" to 9, "문구/오피스" to 10,
        "도서/음악" to 11,"티켓/교환권" to 12, "식품" to 13, "동물/식물" to 14, "영화/공연" to 15, "자동차/공구" to 16, "기타 물품" to 17)
}