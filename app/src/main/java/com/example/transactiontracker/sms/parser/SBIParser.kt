package com.example.transactiontracker.sms.parser

import android.util.Log
import com.example.transactiontracker.sms.model.CashBackCategory
import com.example.transactiontracker.sms.model.ParsedTransaction
import com.example.transactiontracker.sms.model.TransactionType

class SBIParser : BaseSmsParser() {
    val forbiddenKeywords = listOf(
        "statement",
        "online payment of",
        "otp",
        "one-time",
        "declined",
        "we tried calling you to confirm the trxn.",
        "standing instruction",
        "request"
    )


    override fun canHandle(sender: String, sms: String): Boolean {
        val containsForbidden = forbiddenKeywords.any {
            sms.contains(it, ignoreCase = true)
        }
        return (sender.contains("sbi", ignoreCase = true)
                || sender.contains("sbi bank", ignoreCase = true)
                || sender.contains("9315926219", ignoreCase = true))
                && !containsForbidden
    }

    override fun parse(sms: String, date: Long): ParsedTransaction? {
        var cashback: Pair<Int, CashBackCategory> = Pair(0, CashBackCategory.NA)
        var amount = extractAmount(sms)
        var last4 = extractCardLast4(sms)
        var merchant = extractMerchant(sms)
        val type = detectType(sms)

        if (type == TransactionType.CREDIT) {
            last4 = "8622"
            merchant = "Paid to SBi card"
        }
//        Log.d("tanmay", "parse: sms is " + sms)
//        Log.d("tanmay", "parse: SBI  $amount  $last4  $merchant  $type")

        if (amount == null || last4 == null || merchant == "Unknown Merchant") {
            return null
        }

        amount = type.applySign(amount)

        if (type != TransactionType.CREDIT) {
            cashback = getCashBackAmount(amount, sms)
//            Log.d("tanmay", "getCashbackAmount: $cashback  $amount  $sms")
        }

        return ParsedTransaction(
            merchant = merchant,
            amount = amount,
            cardNo = last4,
            type = type,
            date = date,
            bankName = "SBI",
            sms = sms,
            cashback = cashback.first,
            cashBackCategory = cashback.second
        )
    }

    val list10 = listOf(
        "phone pe",
        "phonepe",
        "ppbbps"
    )

    private fun getCashBackAmount(amount: Double, sms: String): Pair<Int, CashBackCategory> {
        return when {
            sms.contains("fuel", true) -> {
                Pair(
                    0,
                    CashBackCategory.NA
                )
            }

            list10.any {
                sms.contains(it, true)
            } -> {
                Pair(
                    amount.div(100).toInt().times(10),
                    CashBackCategory.PHONE_PE
                )
            }

            sms.contains("upi", true) -> {
                Pair(
                    amount.div(100).toInt().times(1),
                    CashBackCategory.UPI
                )
            }

            else -> Pair(amount.div(100).toInt().times(5), CashBackCategory.ONLINE)
        }
    }

    fun extractMerchant(sms: String): String {
        return when {
            sms.contains("at", true) -> {
                sms.substringAfter(" at ", " ")
                    .substringBefore(" (")
                    .substringBefore(" on ")
                    .trim()
            }

            else -> "Unknown Merchant"
        }
    }
}
