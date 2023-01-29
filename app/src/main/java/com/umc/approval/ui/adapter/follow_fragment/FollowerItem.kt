package com.umc.approval.ui.adapter.follow_fragment

data class FollowerItem (val isFollowing : Int, val name : String, val rank : Int){
    companion object{
        const val FOLLOWING = 0
        const val UNFOLLOWING = 1
    }
}