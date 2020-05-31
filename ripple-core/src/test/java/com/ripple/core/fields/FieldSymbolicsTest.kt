package com.ripple.core.fields

import com.ripple.core.formats.Format
import com.ripple.core.formats.LEFormat
import com.ripple.core.formats.TxFormat
import com.ripple.core.serialized.enums.EngineResult
import com.ripple.core.serialized.enums.LedgerEntryType
import com.ripple.core.serialized.enums.TransactionType
import com.ripple.core.types.known.sle.LedgerEntry
import com.ripple.core.types.known.sle.LedgerHashes
import com.ripple.core.types.known.sle.entries.*
import com.ripple.core.types.known.tx.Transaction
import com.ripple.core.types.known.tx.txns.*
import com.ripple.core.types.known.tx.txns.pseudo.EnableAmendment
import com.ripple.core.types.known.tx.txns.pseudo.SetFee
import com.ripple.utils.TestHelpers
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import org.junit.Assert.*
import org.junit.Test
import java.lang.reflect.Method
import java.util.*

class FieldSymbolicsTest {
    companion object {
        private val txs = TreeMap<TransactionType, Class<out Transaction>>()
        private val les = TreeMap<LedgerEntryType, Class<out LedgerEntry>>()

        init {

            les[LedgerEntryType.AccountRoot] = AccountRoot::class.java
            les[LedgerEntryType.DirectoryNode] = DirectoryNode::class.java
            les[LedgerEntryType.RippleState] = RippleState::class.java
            les[LedgerEntryType.Check] = Check::class.java
            les[LedgerEntryType.Escrow] = Escrow::class.java
            les[LedgerEntryType.Offer] = Offer::class.java
            les[LedgerEntryType.LedgerHashes] = LedgerHashes::class.java
            les[LedgerEntryType.Amendments] = Amendments::class.java
            les[LedgerEntryType.FeeSettings] = FeeSettings::class.java
            les[LedgerEntryType.Ticket] = Ticket::class.java
            les[LedgerEntryType.SignerList] = SignerList::class.java
            les[LedgerEntryType.PayChannel] = PayChannel::class.java
            les[LedgerEntryType.DepositPreauth] = DepositPreauthLe::class.java

            txs[TransactionType.Payment] = Payment::class.java
            txs[TransactionType.CheckCreate] = CheckCreate::class.java
            txs[TransactionType.CheckCash] = CheckCash::class.java
            txs[TransactionType.CheckCancel] = CheckCancel::class.java
            txs[TransactionType.AccountSet] = AccountSet::class.java
            txs[TransactionType.SetRegularKey] = SetRegularKey::class.java
            txs[TransactionType.TrustSet] = TrustSet::class.java
            txs[TransactionType.OfferCancel] = OfferCancel::class.java
            txs[TransactionType.OfferCreate] = OfferCreate::class.java
            txs[TransactionType.TicketCancel] = TicketCancel::class.java
            txs[TransactionType.TicketCreate] = TicketCreate::class.java
            txs[TransactionType.EscrowCancel] = EscrowCancel::class.java
            txs[TransactionType.EscrowCreate] = EscrowCreate::class.java
            txs[TransactionType.EscrowFinish] = EscrowFinish::class.java
            txs[TransactionType.SignerListSet] = SignerListSet::class.java
            txs[TransactionType.PaymentChannelCreate] = PaymentChannelCreate::class.java
            txs[TransactionType.PaymentChannelFund] = PaymentChannelFund::class.java
            txs[TransactionType.PaymentChannelClaim] = PaymentChannelClaim::class.java
            txs[TransactionType.EnableAmendment] = EnableAmendment::class.java
            txs[TransactionType.SetFee] = SetFee::class.java
            txs[TransactionType.DepositPreauth] = DepositPreauth::class.java
            txs[TransactionType.AccountDelete] = AccountDelete::class.java

            for (tt in txs.keys) {
                val aClass = txs[tt]!!
                assertEquals(aClass.simpleName, tt.name)
            }
            for (let in les.keys) {
                val simpleName = les[let]!!.simpleName
                if (simpleName == "DepositPreauthLe") {
                    assertEquals("DepositPreauth", let.name)
                } else {
                    assertEquals(simpleName, let.name)
                }
            }
        }
    }

    @Test
    fun checkProtocolDefinitions() {
        val reader = TestHelpers.getResourceReader("protocol.json")
        val o = JSONObject(JSONTokener(reader))

        checkFields(o.getJSONArray("fields"))
        checkEngineResults(o.getJSONObject("engineResults"))
        checkTransactionTypes(o.getJSONArray("transactions"))
        checkLedgerEntries(o.getJSONArray("ledgerEntries"))
    }

    private fun checkFields(fields: JSONArray) {
        val names = TreeSet<String>()
        for (i in 0 until fields.length()) {
            val fieldJson = fields.getJSONObject(i)
            val nam = fieldJson.getString("name")
            if (nam.isEmpty()) {
                continue
            }

            names.add(nam)
            try {
                val f = Field.valueOf(fieldJson.getString("name"))
                val t = Type.valueOf(fieldJson.getString("type"))
                assertEquals(fieldJson.toString(2),
                        f.isSigningField,
                        fieldJson.getBoolean("isSigningField"))
                assertEquals(
                        fieldJson.toString(2),
                        f.isSerialized(),
                        fieldJson.getBoolean("isBinary"))
                assertEquals(fieldJson.toString(2), f.type.id.toLong(), t.id.toLong())
                assertEquals(fieldJson.toString(2), f.id.toLong(), fieldJson
                        .getInt("ordinal").toLong())
            } catch (e: IllegalArgumentException) {
                fail("Can't find Field or Type for " + fieldJson)
            }
        }

        @Suppress("LoopToCallChain")
        for (field in Field.values()) {
            // We have extra fields declared here
            if (field.isSerialized() && !names.contains(field.name)) {
                if (!(field == Field.ArrayEndMarker || field == Field.ObjectEndMarker))
                    fail(field.toString())
            }
        }
    }


    private fun checkEngineResults(engineResults: JSONObject) {
        val keys = engineResults.keys()
        val results = HashMap<String, EngineResult>()
        val declarations = TreeMap<Int, String>()
        for (r in EngineResult.values()) {
            results[r.name] = r
            assertTrue("No old codes", engineResults.has(r.name))
        }

        while (keys.hasNext()) {
            val key = keys.next() as String
            val resultObj = engineResults.getJSONObject(key)
            val ordinal = resultObj.getInt("ordinal")
            val description = resultObj.getString("description")
            val declaration = makeDeclarationLine(key, ordinal, description)
            declarations[ordinal] = declaration
            assertTrue("missing " + declaration, results.containsKey(key))
            val ter = results[key]!!
            assertEquals(declaration, ter.asInteger().toLong(), ordinal.toLong())
            assertEquals(declaration, ter.human, description)
        }

        for (declaration in declarations.values) {
            println(declaration)
        }
    }

    private fun makeDeclarationLine(key: String, ordinal: Int, description: String): String {
        return if (ordinal >= 0 || Math.abs(ordinal) % 100 == 99) {
            String.format(Locale.US, "%s(%d, \"%s\"),",
                    key, ordinal,
                    description)
        } else {
            String.format(Locale.US, "%s(\"%s\"),", key, description)
        }
    }

    private fun checkTransactionTypes(txns: JSONArray) {
        for (i in 0 until txns.length()) {
            val tx = txns.getJSONObject(i)
            val txName = tx.getString("name")
            val ordinal = tx.getInt("ordinal")

            try {
                val f = TxFormat::class.java.getField(txName)
                assertTrue(java.lang.reflect.Modifier.isStatic(f.modifiers))
            } catch (e: Exception) {
                throw IllegalStateException(
                        "TxFormat is missing named declaration: " + txName)
            }

            try {
                val txType = TransactionType.valueOf(txName)
                if (txType.asInteger() != ordinal) {
                    fail("incorrect ordinal for " +
                            txType + ", expected=" +
                            ordinal + " actual=" +
                            txType.asInteger())
                }

            } catch (e: IllegalArgumentException) {
                fail("missing TransactionType " + txName)
            }

            val txFormat = TxFormat.fromString(txName)
            assertNotNull("missing txName $txName", txFormat)
            checkFormat(tx, txFormat)

            val requirements = txFormat.requirements()

            val key = TransactionType.valueOf(txName)
            assertTrue("FieldSymbolicsTest.txs missing " + key, FieldSymbolicsTest.txs.containsKey(key))
            val kls = FieldSymbolicsTest.txs[key]!!

            checkMethods(txFormat, requirements, kls)


            assertTrue(
                    txName,
                    FieldSymbolicsTest.txs.containsKey(TransactionType.valueOf(txName)))
        }

        assertEquals(txns.length().toLong(),
                TransactionType.values().size.toLong())

    }

    private fun checkMethods(txFormat: Format<*>, requirements: EnumMap<Field, Format.Requirement>, kls: Class<*>) {
        // println("Methods for: " + kls.simpleName)
        var missing = false
        for (field in requirements.keys) {
            val requirement = requirements[field]
            val optional = requirement == Format.Requirement.OPTIONAL
            val fieldType = field.type.name
            if (optional && !txFormat.isCommon(field)) {
                val name = String.format("has%s", field.name)
                val method = getMethod(kls, name, 0)
                if (method == null) {
                    val body = String.format("{return has(%s.%s);}",
                            fieldType, field.name)
                    val start = String.format("public boolean %s() %s",
                            name, body)
                    println(start)
                    missing = true
                }
            }
            if (!txFormat.isCommon(field)) {
                var methodName = camelize(field.name)
                var method = getMethod(kls, methodName, 0)
                if (method == null) {
                    val body = String.format("{return get(%s.%s);}",
                            fieldType, field.name)
                    val start = String.format(
                            "public %s %s() %s",
                            fieldType,
                            methodName,
                            body)
                    println(start)
                    missing = true
                }
                methodName = camelize(field.name)
                method = getMethod(kls, methodName, 1)
                if (method == null) {
                    val body = String.format("{ put(%s.%s, val);}",
                            fieldType, field.name)
                    val declaration = String.format("public void %s(%s val) %s",
                            methodName,
                            fieldType, body)
                    println(declaration)
                    missing = true
                } else {
                    // System.out.println(method.toGenericString());
                }

            }
            assertFalse(missing);
        }
    }

    private fun getMethod(kls: Class<*>, name: String, paramCount: Int): Method? {
        return kls.methods.firstOrNull {
            it.name == name && it.parameterCount == paramCount }
    }

    private fun checkFormat(obj: JSONObject, format: Format<*>?) {
        val txName = obj.getString("name")

        if (format == null) {
            throw IllegalArgumentException()
        }
        val requirements = format.requirements()
        val fields = obj.getJSONArray("fields")

        for (j in 0 until fields.length()) {
            val field = fields.getJSONArray(j)
            val fieldName = field.getString(0)
            val requirement = field.getString(1)

            val key = Field.fromString(fieldName)
            if (!requirements.containsKey(key)) {
                fail(String.format("%s format missing %s %s %n",
                        txName, requirement, fieldName))
            } else {
                val req = requirements[key]
                if (req.toString() != requirement) {
                    fail(String.format("%s format missing %s %s %n",
                            txName, requirement, fieldName))

                }
            }
        }
        // check length is same, and if none are missing, must be equal ;)
        assertEquals(obj.toString(2),
                fields.length().toLong(), requirements.size.toLong())

    }

    private fun camelize(name: String): String {
        return name.substring(0, 1).toLowerCase() + name.substring(1)
    }

    private fun checkLedgerEntries(entries: JSONArray) {
        for (i in 0 until entries.length()) {
            val entryJson = entries.getJSONObject(i)
            val name = entryJson.getString("name")
            val ordinal = entryJson.getInt("ordinal")

            try {
                val f = LEFormat::class.java.getField(name)
                assertTrue(java.lang.reflect.Modifier.isStatic(f.modifiers))
            } catch (e: Exception) {
                throw IllegalStateException(
                        "LEFormat is missing named declaration: " + name)
            }

            if (!name.isEmpty()) {
                try {
                    val let = LedgerEntryType.valueOf(name)
                    if (let.asInteger() != ordinal) {
                        fail("incorrect ordinal for " +
                                let + ", expected=" +
                                ordinal.toChar() + " actual=" +
                                (let.asInteger() as Int).toChar())
                    }
                } catch (e: IllegalArgumentException) {
                    fail("missing LedgerEntryType for " + entryJson)
                }

                val format = LEFormat.fromString(name)
                assertNotNull(format)
                checkFormat(entryJson, format)

                val key = LedgerEntryType.valueOf(name)
                assertTrue(FieldSymbolicsTest.les.containsKey(key))
                val kls = FieldSymbolicsTest.les[key]!!
                // System.out.println("Methods for: " + txName);

                checkMethods(format, format.requirements(), kls)
            }

            if (name.isEmpty()) {
                throw IllegalStateException("name is empty")
            }

        }
        assertEquals(entries.length().toLong(), LedgerEntryType.values().size.toLong())
    }
}
