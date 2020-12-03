package com.ripple.core.serialized

import com.ripple.core.fields.Field
import com.ripple.core.fields.HasField
import com.ripple.core.fields.Type
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class SerializedTypeTest {
    @Test
    fun testStaticMemberConformity() {
        val loader = javaClass.classLoader
        Arrays.stream(Type.values()).filter { it.id in 1..255 }.forEach { type ->
            var pkg = "com.ripple.core.coretypes"
            val typeName = type.toString()

            arrayListOf("hash", "uint").forEach {
                if (typeName.toLowerCase().startsWith(it)) {
                    pkg += (".$it")
                }
            }

            val klassName = "$pkg.$type"
            val klass = getClass(loader, klassName)

            // These will throw if they are missing!
            klass.getDeclaredMethod("fromHex", String::class.java)
            klass.getDeclaredMethod("fromParser", BinaryParser::class.java)
            klass.getDeclaredMethod("fromBytes", ByteArray::class.java)

            Arrays.stream(Field.values())
                  .filter { it.type == type }.forEach { field ->
                try {
                    val declaredField = klass.getDeclaredField(field.toString())
                    val get = declaredField.get(null) as HasField
                    assertEquals(get.field, field)
                } catch (e: Exception) {
                    throw RuntimeException("$type is missing static member $field", e)
                }
            }
        }
    }

    private fun getClass(loader: ClassLoader, kls: String): Class<*> {
        try {
            return loader.loadClass(kls)
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        }

    }
}
