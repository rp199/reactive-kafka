package com.rp199.reactivekafka.handler.model

import java.time.LocalDate

data class OtherMessage(val someDate: LocalDate, val someMessage: String)