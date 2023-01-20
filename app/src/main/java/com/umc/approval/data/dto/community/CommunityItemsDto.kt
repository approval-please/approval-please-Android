package com.umc.approval.data.dto.community

/**Community Items Dto*/
data class CommunityItemsDto (
        var communityItemDto : MutableList<CommunityItemDto> =
                mutableListOf<CommunityItemDto>(),
)