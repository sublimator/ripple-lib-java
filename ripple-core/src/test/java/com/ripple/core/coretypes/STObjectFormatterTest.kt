package com.ripple.core.coretypes

import com.ripple.core.fields.Field
import com.ripple.core.serialized.enums.LedgerEntryType
import com.ripple.core.serialized.enums.TransactionType
import com.ripple.core.types.known.sle.LedgerEntry
import com.ripple.core.types.known.sle.entries.DepositPreauthLe
import com.ripple.core.types.known.sle.entries.DirectoryNode
import com.ripple.core.types.known.tx.Transaction
import com.ripple.core.types.known.tx.result.AffectedNode
import com.ripple.core.types.known.tx.result.TransactionMeta
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class STObjectFormatterTest {
    @Test
    fun testLedgerEntryTypes() {
        LedgerEntryType.values().forEach { let ->
            val so = STObject()
            so.put(Field.LedgerEntryType, let)
            val leSo = STObjectFormatter.format(so)
            assertTrue(leSo is LedgerEntry)
            val klassName = leSo.javaClass.simpleName
            when (leSo) {
                is DirectoryNode -> assertTrue(klassName.endsWith("Directory"))
                is DepositPreauthLe -> assertEquals(let.toString() + "Le", klassName)
                else -> assertEquals(let.toString(), klassName)
            }
            val le = leSo as LedgerEntry
            assertEquals(let, le.ledgerEntryType())
        }
    }

    @Test
    fun testTransactionTypes() {
        TransactionType.values().forEach { tt ->
            val so = STObject()
            so.put(Field.TransactionType, tt)
            println("$tt")
            val txSo = STObjectFormatter.format(so)
            assertTrue(txSo is Transaction)
            assertEquals(tt.toString(), txSo.javaClass.simpleName)
            val tx = txSo as Transaction
            assertEquals(tt, tx.transactionType())
        }
    }

    @Test
    fun testTransactionMeta() {
        @Language("JSON")
        val metaJson = """
            {
        "AffectedNodes": [
          {
            "ModifiedNode": {
              "FinalFields": {
                "Account": "rMWUykAmNQDaM9poSes8VLDZDDKEbmo7MX",
                "Balance": "1632282339",
                "Flags": 0,
                "OwnerCount": 19,
                "Sequence": 1771490
              },
              "LedgerEntryType": "AccountRoot",
              "LedgerIndex": "56091AD066271ED03B106812AD376D48F126803665E3ECBFDBBB7A3FFEB474B2",
              "PreviousFields": {
                "Balance": "1632282349",
                "OwnerCount": 18,
                "Sequence": 1771489
              },
              "PreviousTxnID": "7A6E920AA4EFBA202699437539D176D842904B8402A25D344A25C4D24234CFC4",
              "PreviousTxnLgrSeq": 7501325
            }
          },
          {
            "CreatedNode": {
              "LedgerEntryType": "DirectoryNode",
              "LedgerIndex": "6F86B77ADAC326EA25C597BAD08C447FA568D28A2504883F530520669E693000",
              "NewFields": {
                "ExchangeRate": "530520669E693000",
                "RootIndex": "6F86B77ADAC326EA25C597BAD08C447FA568D28A2504883F530520669E693000",
                "TakerGetsCurrency": "0000000000000000000000004C54430000000000",
                "TakerGetsIssuer": "92D705968936C419CE614BF264B5EEB1CEA47FF4",
                "TakerPaysCurrency": "0000000000000000000000004254430000000000",
                "TakerPaysIssuer": "92D705968936C419CE614BF264B5EEB1CEA47FF4"
              }
            }
          },
          {
            "CreatedNode": {
              "LedgerEntryType": "Offer",
              "LedgerIndex": "A32C940A8962A0FB6EA8CDF0DD9F4CE629DEF8EA360E099F8C634AAE351E6607",
              "NewFields": {
                "Account": "rMWUykAmNQDaM9poSes8VLDZDDKEbmo7MX",
                "BookDirectory": "6F86B77ADAC326EA25C597BAD08C447FA568D28A2504883F530520669E693000",
                "OwnerNode": "000000000000405C",
                "Sequence": 1771489,
                "TakerGets": {
                  "currency": "LTC",
                  "issuer": "rNPRNzBB92BVpAhhZr4iXDTveCgV5Pofm9",
                  "value": "15.92"
                },
                "TakerPays": {
                  "currency": "BTC",
                  "issuer": "rNPRNzBB92BVpAhhZr4iXDTveCgV5Pofm9",
                  "value": "0.2297256"
                }
              }
            }
          },
          {
            "ModifiedNode": {
              "FinalFields": {
                "Flags": 0,
                "IndexPrevious": "0000000000000000",
                "Owner": "rMWUykAmNQDaM9poSes8VLDZDDKEbmo7MX",
                "RootIndex": "2114A41BB356843CE99B2858892C8F1FEF634B09F09AF2EB3E8C9AA7FD0E3A1A"
              },
              "LedgerEntryType": "DirectoryNode",
              "LedgerIndex": "D6540D658870F60843ECF33A4F903665EE5B212479EB20E1E43E3744A95194AC"
            }
          }
        ],
        "TransactionIndex": 2,
        "TransactionResult": "tesSUCCESS"
      }
        """

        val meta = STObject.fromJSON(metaJson)
        assertTrue(meta is TransactionMeta)
        assertTrue(meta[STArray.AffectedNodes][0] is AffectedNode)
    }
}
