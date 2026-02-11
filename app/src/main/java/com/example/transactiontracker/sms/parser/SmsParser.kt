package com.example.transactiontracker.sms.parser

import android.util.Log
import com.example.transactiontracker.sms.model.ParsedTransaction
import javax.inject.Inject

class SmsParser @Inject constructor() {
     public  val TAG: String = "tanmay"



    fun parse(smsBody: String, date: Long): ParsedTransaction? {
        val lower = smsBody.lowercase()



        /*if (!lower.contains("hdfc bank card") && !lower.contains("icici bank card") && !lower.contains("sbi credit card")) {
            Log.d(TAG, "Sms not from a supported bank, returning null")
            return null
        }

        var amountRegex: Regex? = null
        var cardRegex: Regex? = null
        var merchantRegex: Regex? = null
        var bankName:String = ""

        if (lower.contains("hdfc bank card")) {
            cardRegex = Regex("card\\s+([0-9]{4})")
            bankName = "HDFC"
            if (lower.contains("by upi")) {
                amountRegex = Regex("txn\\s+rs\\.?\\s*([0-9,]+(?:\\.[0-9]+)?)")
                merchantRegex = Regex("at\\s+([a-z0-9.@\\s]+?)\\s+by")
            } else {
                amountRegex = Regex("spent\\s+rs\\.?\\s*([0-9,]+(?:\\.[0-9]+)?)")
                merchantRegex = Regex("at\\s+([a-z0-9\\s]+?)\\s+on")
            }
        } else if (lower.contains("icici bank card")) {
            amountRegex = Regex("(?:inr|rs)\\s*([0-9,]+(?:\\.[0-9]{1,2})?)\\s+spent")
            cardRegex = Regex("card\\s+xx([0-9]{4})")
            merchantRegex = Regex("\\s+(?:on|at)\\s+([A-Za-z0-9\\s]+?)\\.")
            bankName = "ICICI"
        } else if (lower.contains("sbi credit card")) {
            amountRegex = Regex("rs\\.?\\s*([0-9,]+(?:\\.[0-9]+)?)")
            cardRegex = Regex("ending\\s+with\\s+([0-9]{4})")
            bankName = "SBI"
            merchantRegex = Regex("at\\s+([a-z0-9\\s]+?)\\s+on")
        }


        if (amountRegex == null) {
            Log.d(TAG, "parse: amounti s null")
            return null
        }

        Log.d(TAG, "parse: " + lower)

        val amountString = amountRegex.find(lower)?.groupValues?.get(1)?.replace(",", "")
        val amount = amountString?.toDoubleOrNull() ?: 0.0

        val cardLast4 = cardRegex?.find(lower)?.groupValues?.get(1) ?: "0000"
        val merchant = merchantRegex?.find(lower)?.groupValues?.get(1)?.trim() ?: "Unknown Merchant"


        Log.d(TAG, "Parsed: Amount=$amount, Card=$cardLast4, Merchant='$merchant'")
*/

        return null
    }
}