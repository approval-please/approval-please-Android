package com.umc.approval.util

object Utils {

    /*camera*/
    val PICK_IMAGE_FROM_GALLERY = 1000
    val PICK_IMAGE_FROM_GALLERY_PERMISSION = 1010

    /*category*/
    val categoryMap = mapOf<Int, String>(0 to "디지털기기", 1 to "생활가전", 2 to "생활용품", 3 to "가구/인테리어", 4 to "주방/건강",
        5 to "출산/유아동", 6 to "패션의류/잡화", 7 to "뷰티/미용", 8 to "스포츠/헬스/레저", 9 to "취미/게임/완구", 10 to "문구/오피스",
        11 to "도서/음악", 12 to "티켓/교환권", 13 to "식품", 14 to "동물/식물", 15 to "영화/공연", 16 to "자동차/공구", 17 to "기타물품")

    /*category*/
    val categoryMapReverse = mapOf<String, Int>("디지털기기" to 0,"생활가전" to 1,"생활용품" to 2,"가구/인테리어" to 3,"주방/건강" to 4,
        "출산/유아동" to 5, "패션의류/잡화" to 6, "뷰티/미용" to 7, "스포츠/헬스/레저" to 8, "취미/게임/완구" to 9, "문구/오피스" to 10,
        "도서/음악" to 11,"티켓/교환권" to 12, "식품" to 13, "동물/식물" to 14, "영화/공연" to 15, "자동차/공구" to 16, "기타물품" to 17)

    /*status*/
    val statusMap = mapOf<String, Int>("승인됨" to 0,"반려됨" to 1,"결재 대기중" to 2,"상태 전체" to 3)

    /*sort*/
    val sortByMap = mapOf<String, Int>("인기순" to 0,"최신순" to 2)

    /*searchSort*/
    val searchSortMap = mapOf<String, Int>("최신순" to 0, "인기순" to 1)

    val level = mapOf<Int, String>(0 to "사원", 1 to "주임", 2 to "대리",
        3 to "차장", 4 to "과장", 5 to "부장")

    val levelImage = mapOf<Int, String>(0 to "https://approval-please.s3.ap-northeast-2.amazonaws.com/0.png", 1 to "https://approval-please.s3.ap-northeast-2.amazonaws.com/1.png", 2 to "https://approval-please.s3.ap-northeast-2.amazonaws.com/2.png",
        3 to "https://approval-please.s3.ap-northeast-2.amazonaws.com/3.png", 4 to "https://approval-please.s3.ap-northeast-2.amazonaws.com/4.png", 5 to "https://approval-please.s3.ap-northeast-2.amazonaws.com/5.png")
}