package com.example.myapplication

class FirestationDTO {
    //    val entid: String? = null,
//    val sigungu: String? = null,
    private var title: String? = null
    //    val delegatee: String? = null,
    private var roadaddress: String? = null
//    val homepage: String? = null,
//    val tel: String? = null,
//    val fax: String? = null
    constructor(title: String, roadaddress: String){
        this.title = title
        this.roadaddress = roadaddress
    }
    fun setTitle(title: String) {
        this.title = title
    }
    fun setRoadaddress(roadaddress: String) {
        this.roadaddress = roadaddress
    }

    fun getTitle(): String? {
        return this.title
    }
    fun getRoadaddress(): String? {
        return this.roadaddress
    }
}