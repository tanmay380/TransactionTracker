package com.example.transactiontracker.sms.parser

import android.util.Log
import com.example.transactiontracker.sms.model.ParsedTransaction

class HdfcParser : BaseSmsParser() {

    val forbiddenKeywords = listOf(
        "statement",
        "online payment of",
        "otp",
        "one-time",
        "declined",
        "online payment of",
        "standing instruction",
        "request"
    )

    override fun canHandle(sender: String, sms: String): Boolean {
        val containsForbidden = forbiddenKeywords.any { sms.contains(it, ignoreCase = true) }
        return (sender.contains("hdfc", ignoreCase = true) ||
                sender.contains("hdfc bank", ignoreCase = true))
                && !containsForbidden
    }

    override fun parse(sms: String, date: Long): ParsedTransaction? {
        val amount = extractAmount(sms)
        val last4 = extractCardLast4(sms)
        val merchant = extractMerchant(sms)
        val type = detectType(sms)


        Log.d("tanmay", "parse: sms is " + sms)
        Log.d("tanmay", "parse: hdfcparser $amount  $last4  $merchant  $type")
        if (amount == null || last4 == null || merchant == "Unknown Merchant") {
            return null
        }

        return ParsedTransaction(
            merchant = merchant,
            amount = amount?.toDouble(),
            cardNo = last4,
            type = type,
            date = date,
            bankName = "HDFC",
            sms = sms
        )
    }

    fun extractMerchant(sms: String): String {
        return when {
            sms.contains("at", true) -> {
                sms.substringAfter("at ", " ")
                    .substringBefore("by ")
                    .substringBefore("on ")
                    .trim()
            }

            else -> "Unknown Merchant"
        }
    }

}

/*
Txn Rs.1304.10
On HDFC Bank Card 2640
At oniisaab926756.rzp@rxaxis
by UPI 109149842126
On 08-02
Not You?
Call 18002586161/SMS BLOCK CC 2640 to 7308080808

Sent Rs.1060.64
From HDFC Bank A/C *3512
To Airtel Mobile Bill
On 04/07/25
Ref 518509873801
Not You?
Call 18002586161/SMS BLOCK UPI to 7308080808


Spent Rs.8914 On HDFC Bank Card 2640 At TATAPAYMENTSLIMITE    GUR On 2025-07-24:11:25:58.Not You? To Block+Reissue Call 18002586161/SMS BLOCK CC 2640 to 7308080808


 */