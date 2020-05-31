package com.ripple.core.types.shamap

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.ripple.core.binary.STReader
import com.ripple.core.coretypes.hash.Hash256
import com.ripple.core.types.known.tx.Transaction
import com.ripple.core.types.known.tx.result.TransactionMeta
import com.ripple.core.types.known.tx.result.TransactionResult
import com.ripple.encodings.common.B16
import com.ripple.encodings.json.JSON
import com.ripple.utils.TestHelpers
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

fun ShaMapNode.repr(): String {
    val strings = ArrayList<String>()
    toBytesSink {
        strings.add(B16.encode(it))
    }

    val typeName = javaClass.simpleName

    if (this is ShaMapLeaf) {
        val ix = strings.size - 1
        val index = strings[ix]
        strings.removeAt(ix)
        val all = strings.joinToString("")
                .windowed(64, 64, true)
                .joinToString("\n\t")
        strings.clear()
        strings.add(all)
        strings.add(index)
    }
    val depth = when (this) {
        is ShaMapInner -> {
            depth
        } else -> {
            null
        }
    }

    return "$typeName(depth=$depth hash=${hash()})\n" +
            "\t${B16.encode(hashPrefix().bytes())}\n\t" +
            strings.joinToString("\n\t")
}

class ShaMapReadmeTest {
    @Test
    fun createFixtures() {
        val txId = "FEEE5CC92B64375C8FEE56D54A82B9965E44FE0DCF673DBF27D0AA93F8AFF4FB"
        val dumpFile = "ledger-transactions-only-36110226.json"
        val reader = TestHelpers.getResourceReader(dumpFile)
        val dump = JSON.parseObject(reader)
        val transactions = dump.get("transactions") as ArrayNode
        val tree = TransactionTree()
        transactions.forEach {
            val obj = it as ObjectNode
            obj.replace("ledger_index", dump["ledger_index"])
            val result = TransactionResult.fromJSON(obj)
            tree.addTransactionResult(result)
        }
        Assert.assertEquals(dump["transaction_hash"].asText(),
                tree.hash().toHex())
        val path = tree.pathToIndex(Hash256.fromHex(txId))
        path.topDownList().forEach({ node ->
            println(node.repr() + "\n")
        })
    }

    @Test
    fun parseFixture() {
        val fixture = """
            C11C12000722800000002400AA7BFF201900AA7BFC201B0226FF9464D4461B5C
            A191A906000000000000000000000000455448000000000006CC4A6D023E68AA
            3499C6DE3E9F2DC52B8BA25465400000000861014A6840000000000000C17321
            03CF1DFB34A96363FF2B91638FCE51E6D7B88419729E9A81ABC99A9512FFB9C7
            3374463044022005ED4635AE246A4060378D9396CAA0E42F7E9129D309B2E98B
            DF5CE2352520A002207D4494019EA4327F4087EA34B05CE9E5CD0AE3082BAD77
            CC3698CE5A59C21BD581140BEC53D0830ADCE9E372086E570809916C440E83C3
            F4201C00000033F8E311006F561BF352EEDB9072286286A4BBC8C419C995A171
            0AA0CFA8B0D2B843F07D294F60E82400AA7BFF501090B86A84C7F7843673BCF8
            2E565E69498CAEF463F8055ABA4C04581E76C9270064D4461B5CA191A9060000
            00000000000000000000455448000000000006CC4A6D023E68AA3499C6DE3E9F
            2DC52B8BA25465400000000861014A81140BEC53D0830ADCE9E372086E570809
            916C440E83E1E1E31100645690B86A84C7F7843673BCF82E565E69498CAEF463
            F8055ABA4C04581E76C92700E8364C04581E76C927005890B86A84C7F7843673
            BCF82E565E69498CAEF463F8055ABA4C04581E76C92700011100000000000000
            00000000004554480000000000021106CC4A6D023E68AA3499C6DE3E9F2DC52B
            8BA254E1E1E41100645690B86A84C7F7843673BCF82E565E69498CAEF463F805
            5ABA4C045835BF30CCBFE72200000000364C045835BF30CCBF5890B86A84C7F7
            843673BCF82E565E69498CAEF463F8055ABA4C045835BF30CCBF011100000000
            00000000000000004554480000000000021106CC4A6D023E68AA3499C6DE3E9F
            2DC52B8BA2540311000000000000000000000000000000000000000004110000
            000000000000000000000000000000000000E1E1E411006F56AE4B62A73DC540
            40E523FCE863981AB601B6D6BCD3CFCE303B43A430FC6D4DB1E7220000000024
            00AA7BFC250226FF9133000000000000000034000000000000000055B61A418E
            421021CFBA22B02F4934B4F79B1091F91CF910AEB21DA0975CEC52AB501090B8
            6A84C7F7843673BCF82E565E69498CAEF463F8055ABA4C045835BF30CCBF64D4
            461B7D5C693EDF000000000000000000000000455448000000000006CC4A6D02
            3E68AA3499C6DE3E9F2DC52B8BA25465400000000861014A81140BEC53D0830A
            DCE9E372086E570809916C440E83E1E1E511006456B937CC88FCAF18886CC8D4
            B19A5326F56B0B84E06AE511407B072DE348E05376E722000000003100000000
            0000000032000000000000000058B937CC88FCAF18886CC8D4B19A5326F56B0B
            84E06AE511407B072DE348E0537682140BEC53D0830ADCE9E372086E57080991
            6C440E83E1E1E5110061250226FF915558E2CDAB1D8D5477D8FED6EA5EFD9F9F
            D8FAC56EA907E94197900C6C6175CE9556E9293AF964F2B20467530673C8F327
            41ED03DEF52F6B6625B4D837C155856C26E62400AA7BFF6240000000B60EBC57
            E1E722000000002400AA7C002D000000186240000000B60EBB9681140BEC53D0
            830ADCE9E372086E570809916C440E83E1E1F1031000
        """
        val reader = STReader(fixture)
        assertTrue(reader.vlStObject() is Transaction)
        assertTrue(reader.vlStObject() is TransactionMeta)
    }
}
