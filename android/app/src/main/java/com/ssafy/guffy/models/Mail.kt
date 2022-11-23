package com.ssafy.guffy.models

data class Mail (
    val email: String,
    val title: String,
    val message: String
){
    constructor() : this("", "", "")
    constructor(email: String) : this(email, "", "")
}