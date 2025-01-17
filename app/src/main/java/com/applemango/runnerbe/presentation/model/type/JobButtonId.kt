package com.applemango.runnerbe.presentation.model.type

import com.applemango.runnerbe.R

enum class JobButtonId(val id: Int, val job: String, val koreanName: String) {
    PSV(R.id.psv_btn, "PSV","공무원"),
    EDU(R.id.edu_btn,"EDU","교육"),
    DEV(R.id.dev_btn,"DEV","개발"),
    PSM(R.id.psm_btn,"PSM","기획/전략/경영"),
    DES(R.id.des_btn,"DES","디자인"),
    MPR(R.id.mpr_btn,"MPR","마케팅/PR"),
    SER(R.id.ser_btn,"SER","서비스"),
    PRO(R.id.pro_btn,"PRO","생산"),
    RES(R.id.res_btn,"RES","연구"),
    SAF(R.id.saf_btn,"SAF","영업"),
    MED(R.id.med_btn,"MED","의료"),
    HUR(R.id.hur_btn,"HUR","인사"),
    ACC(R.id.acc_btn,"ACC","재무"),
    CUS(R.id.cus_btn,"CUS","CS");

    companion object {
        fun findByJob(job: String) = values().find { it.job == job }
        fun findById(id: Int) = values().find { it.id == id }
        fun findByName(name: String) = values().find { it.koreanName == name }
    }
}