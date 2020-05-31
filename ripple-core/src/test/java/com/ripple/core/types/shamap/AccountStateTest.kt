package com.ripple.core.types.shamap

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.ripple.core.types.known.tx.result.TransactionResult
import com.ripple.encodings.json.JSON
import com.ripple.utils.TestHelpers
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.util.*

class AccountStateTest {

     @Test
    fun loadFromLedgerDump() {
        val file = File("/Users/ndudfield/ripple/rippled/ledger.json")

        if (file.exists()) {
            println(Date())
            val state = AccountState.loadFromLedgerDump(file.path)
            assertEquals(
                    "F47142281FEC506E179E110044626662DA05AC72CBEB16632B8F52A3E3971548",
                    state.hash().toHex())
            println(Date())
        }
    }

    @Test
    fun ledger38129() {
        createTest("ledger-full-38129.json",
                "2C23D15B6B549123FB351E4B5CDE81C564318EB845449CD43C3EA7953C4DB452")
    }

    @Test
    fun ledger40000() {
        createTest("ledger-full-40000.json",
                "1B536BFBDFC92B9550F2F63D32F7269D451885FFB2CAB374332EBC2D663320E0")
    }

    @Test
    fun ledger7501326() {
        val dumpFile = "ledger-transactions-only-7501326.json"
        val reader = TestHelpers.getResourceReader(dumpFile)
        val dump = JSON.parseObject(reader)
        val transactions = dump.get("transactions") as ArrayNode
        val tree = TransactionTree()
        transactions.forEach {
            val obj: ObjectNode = it as ObjectNode
            obj.replace("ledger_index", dump["ledger_index"])
            val result = TransactionResult.fromJSON(obj)
            // TODO: Stopgap: This will exercise the API
            exerciseAPI(result)
            tree.addTransactionResult(result)
        }
        assertEquals(dump["transaction_hash"].asText(),
                tree.hash().toHex())
    }

    private fun exerciseAPI(result: TransactionResult) {
        result.createdAccount()
        result.initiatingAccount()
        result.modifiedRoots()
    }

    private fun createTest(ledger: String, expectedHash: String) {
        val parsed =
                JSON.parseObject(TestHelpers.getResourceReader(ledger))
        val resourceReader =
                TestHelpers.getResourceReader(ledger)

        val accountState = AccountState.loadFromLedgerDump(resourceReader)
        assertEquals(expectedHash,
                parsed["account_hash"].asText())
        assertEquals(expectedHash,
                accountState.hash().toHex())
    }


}
