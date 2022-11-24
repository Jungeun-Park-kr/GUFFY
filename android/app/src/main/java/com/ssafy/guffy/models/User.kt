package com.ssafy.guffy.models

data class User(
    val email: String,
    val gender: String,
    val id: Int,
    var interest1: String,
    var interest2: String,
    var interest3: String,
    var interest4: String,
    var interest5: String,
    var mbti: String,
    val nickname: String,
    var pw: String,
    val token:String,
) {
    constructor() : this(
        "", "", 0, "", "", "", "", "", "", "", "",""
    )

    constructor(email: String, pw: String) : this(
        email, "", 0, "", "", "", "", "", "", "", pw, ""
    )

    constructor(email: String, pw: String, token: String) : this(
        email, "", 0, "", "", "", "", "", "", "", pw, token
    )
}