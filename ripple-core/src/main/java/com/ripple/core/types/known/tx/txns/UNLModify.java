package com.ripple.core.types.known.tx.txns;

import com.ripple.core.coretypes.Blob;
import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.coretypes.uint.UInt8;
import com.ripple.core.serialized.enums.TransactionType;
import com.ripple.core.types.known.tx.Transaction;

public class UNLModify extends Transaction {
    public UNLModify() {
        super(TransactionType.UNLModify);
    }
    public UInt32 ledgerSequence() {return get(UInt32.LedgerSequence);}
    public void ledgerSequence(UInt32 val) { put(UInt32.LedgerSequence, val);}

    public Blob uNLModifyValidator() {return get(Blob.UNLModifyValidator);}
    public void uNLModifyValidator(Blob val) { put(Blob.UNLModifyValidator, val);}

    public UInt8 uNLModifyDisabling() {return get(UInt8.UNLModifyDisabling);}
    public void uNLModifyDisabling(UInt8 val) { put(UInt8.UNLModifyDisabling, val);}
}
