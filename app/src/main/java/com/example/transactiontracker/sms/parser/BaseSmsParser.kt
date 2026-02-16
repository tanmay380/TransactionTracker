package com.example.transactiontracker.sms.parser

import com.example.transactiontracker.sms.model.TransactionType

abstract class BaseSmsParser : SmsParserInterface {

    protected fun extractAmount(sms: String): Double? {
        val regex = Regex("""(?i)(rs\.?|inr)\s?([\d,]+\.?\d*)""")
        val match = regex.find(sms) ?: return null

        return match.groupValues[2]
            .replace(",", "")
            .toDoubleOrNull()
    }

    protected fun extractCardLast4(sms: String): String? {
        val regex = Regex(
            """(Card|A/C|\*|ending|XX)\s?(with)?\s?(\d{4})""",
            RegexOption.IGNORE_CASE
        )

        val match = regex.find(sms) ?: return null
        return match.groupValues.last()
    }

    protected fun detectType(sms: String): TransactionType {
        return when {
            sms.contains("reversal", true) -> TransactionType.REFUND
            sms.contains("refund", true) -> TransactionType.REFUND
            sms.contains("received", true) -> TransactionType.CREDIT
            sms.contains("credited", true) -> TransactionType.CREDIT
            else -> TransactionType.DEBIT
        }
    }
}