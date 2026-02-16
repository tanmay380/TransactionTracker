package com.example.transactiontracker.sms.parser

import android.util.Log
import com.example.transactiontracker.sms.model.ParsedTransaction
import com.example.transactiontracker.sms.model.TransactionType

class ICICIParser : BaseSmsParser() {
    val forbiddenKeywords = listOf(
        "statement",
        "online payment of",
        "otp",
        "one-time",
        "declined",
        "standing instruction",
        "request"
    )


    override fun canHandle(sender: String, sms: String): Boolean {
        val containsForbidden = forbiddenKeywords.any {
            sms.contains(it, ignoreCase = true)
        }
        return (sender.contains("icici", ignoreCase = true)
                || sender.contains("icici bank", ignoreCase = true))
                && !containsForbidden
    }

    override fun parse(sms: String, date: Long): ParsedTransaction? {
        val amount = extractAmount(sms)
        val last4 = extractCardLast4(sms)
        var merchant = extractMerchant(sms)
        var type = detectType(sms)

        if (type == TransactionType.CREDIT){
            merchant = "paid to card"
        }

        Log.d("tanmay", "parse: sms is " + sms)
        Log.d("tanmay", "parse: ICICI  $amount  $last4  $merchant  $type")
        if (amount == null || last4 == null || merchant == "Unknown Merchant"){
            return null
        }


        return ParsedTransaction(
            merchant = merchant,
            amount = type.applySign(amount),
            cardNo = last4,
            type = type,
            date = date,
            bankName = "ICICI",
            sms = sms
        )
    }

    fun extractMerchant(sms: String): String {
        return when {
            sms.contains(" at ", true)->{
                sms.substringAfter(" at ", " ")
                    .substringBefore(" in ")
                    .substringBefore(" avl ")
                    .trim()
            }
            sms.contains(" on ", true) -> {
                sms.substringAfterLast(" on ", "")
                    .substringBefore(" avl ")
                    .substringBefore(" in ")
                    .trim()
            }
            else -> "Unknown Merchant"
        }
    }
}

/*

canHandle: AD-ICICIT-S false
parse: sms is payment of rs 2,599.98 has been received on your icici bank credit card xx1005 through bharat bill payment system on 24-jan-26.
parse: ICICI  2599.98  1005  your icici bank credit card xx1005 through bharat bill payment system on 24-jan-26.  DEBIT
canHandle: payment of rs 19,600.00 has been received on your icici bank credit card xx4008 through bharat bill payment system on 24-jan-26.
canHandle: AD-ICICIT-S false
parse: sms is payment of rs 19,600.00 has been received on your icici bank credit card xx4008 through bharat bill payment system on 24-jan-26.

*/