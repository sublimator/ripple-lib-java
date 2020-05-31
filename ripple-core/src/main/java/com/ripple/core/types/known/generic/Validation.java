package com.ripple.core.types.known.generic;

import com.ripple.core.coretypes.Blob;
import com.ripple.core.coretypes.STObject;
import com.ripple.core.coretypes.hash.Hash256;
import com.ripple.core.coretypes.uint.UInt32;

/*
src/ripple/protocol/impl/STValidation.cpp-34-    static SOTemplate const format{
src/ripple/protocol/impl/STValidation.cpp-35-        {sfFlags, soeREQUIRED},
src/ripple/protocol/impl/STValidation.cpp-36-        {sfLedgerHash, soeREQUIRED},
src/ripple/protocol/impl/STValidation.cpp-37-        {sfLedgerSequence, soeREQUIRED},
src/ripple/protocol/impl/STValidation.cpp-38-        {sfCloseTime, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp-39-        {sfLoadFee, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp-40-        {sfAmendments, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp-41-        {sfBaseFee, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp-42-        {sfReserveBase, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp-43-        {sfReserveIncrement, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp-44-        {sfSigningTime, soeREQUIRED},
src/ripple/protocol/impl/STValidation.cpp-45-        {sfSigningPubKey, soeREQUIRED},
src/ripple/protocol/impl/STValidation.cpp-46-        {sfSignature, soeREQUIRED},
src/ripple/protocol/impl/STValidation.cpp-47-        {sfConsensusHash, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp-48-        {sfCookie, soeDEFAULT},
src/ripple/protocol/impl/STValidation.cpp-49-        {sfValidatedHash, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp:50:        {sfServerVersion, soeOPTIONAL},
src/ripple/protocol/impl/STValidation.cpp-51-    };
 */
public class Validation extends STObject {
    public static boolean isValidation(STObject source) {
        return source.has(UInt32.LedgerSequence) &&
                source.has(UInt32.SigningTime) &&
                source.has(Hash256.LedgerHash) &&
                source.has(Blob.Signature);
    }
}
