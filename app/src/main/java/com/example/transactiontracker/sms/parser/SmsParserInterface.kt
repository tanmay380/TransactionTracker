package com.example.transactiontracker.sms.parser

import com.example.transactiontracker.sms.model.ParsedTransaction

interface SmsParserInterface {
    fun canHandle(sender :String, sms: String): Boolean
    fun parse(sms: String, date : Long) : ParsedTransaction?
}