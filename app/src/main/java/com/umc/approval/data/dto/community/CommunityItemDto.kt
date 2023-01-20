package com.umc.approval.data.dto.community

/**Community Item Dto*/
data class CommunityItemDto (

        var type : Int, //talk, report 구분 값
        var nickname : String, //talk, report
        var profileImage : String, //talk
        var rank : String, //talk, report
        var title : String, //talk
        var title_body : String, //talk
        var body : String, //talk, report
        var vote : String, //talk
        var vote_num : String, //talk
        var image : String, //talk, report
        var images : MutableList<String> = mutableListOf<String>(), //report
        var link : String, //talk, report
        var like : Int, //talk, report
        var scrap : Int, //talk, report
        var views : Int, //talk, report
        var replys : Int, //talk, report
)