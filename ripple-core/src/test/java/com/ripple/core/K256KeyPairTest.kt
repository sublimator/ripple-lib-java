package com.ripple.core

import com.ripple.core.coretypes.AccountID
import com.ripple.crypto.Seed
import com.ripple.crypto.ecdsa.K256
import com.ripple.crypto.ecdsa.K256KeyPair
import com.ripple.encodings.common.B16
import org.bouncycastle.util.encoders.Hex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class K256KeyPairTest {
    private val keyPair = K256.createKeyPair(
            TestFixtures.master_seed_bytes, 0) as K256KeyPair

    @Test
    fun testVerify() {
        assertTrue(keyPair.verifyHash(TestFixtures.master_seed_bytes,
                Hex.decode(TestFixtures.singed_master_seed_bytes)))
    }

    @Test
    fun testDezuz() {
        val kp = Seed.fromBase58("saNyQrVezXdAaXGeyTXTQasWFKGbV").keyPair()
        val address = AccountID.fromKeyPair(kp)
        val expected = "rEMZv3FvBeqRdHnCdpygPMMerN1mqAhQS2"
        assertEquals(expected, address.toString())
    }

    @Test
    fun sanityTestSignAndVerify() {
        val sigBytes = keyPair.signHash(TestFixtures.master_seed_bytes)
        val actualHex = hex(sigBytes)
        val expectedDeterministic = "304402203B72E92DFA98C9DE326B987690785EA390BE80BFD0D0B3A4E3273BC035A8AAAF02207406ABF0AB4649F4C63B9E1AD134D7FEF346FAF5E0FDA91146175C8835529421"
        assertEquals(expectedDeterministic, actualHex)
        assertTrue(keyPair.verifyHash(TestFixtures.master_seed_bytes, sigBytes))
    }

    @Test
    fun testDerivationFromSeedBytes() {
        assertEquals("0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020",
                hex(keyPair.canonicalPubBytes()))
        assertEquals("1ACAAEDECE405B2A958212629E16F2EB46B153EEE94CDD350FDEFF52795525B7",
                hex(keyPair.privateKey()))
    }

    @Test
    fun testDerivationFromString() {
        val keyPairFromSeed = Seed.getKeyPair(TestFixtures.master_seed)
        assertEquals("0330E7FC9D56BB25D6893BA3F317AE5BCF33B3291BD63DB32654A313222F7FD020",
                hex(keyPairFromSeed.canonicalPubBytes()))
        assertEquals("1ACAAEDECE405B2A958212629E16F2EB46B153EEE94CDD350FDEFF52795525B7",
                hex(keyPairFromSeed.privateKey()))
    }

    private fun hex(bytes: ByteArray): String {
        return B16.encode(bytes)
    }
}
