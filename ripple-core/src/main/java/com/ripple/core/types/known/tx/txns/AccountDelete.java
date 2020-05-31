package com.ripple.core.types.known.tx.txns;

import com.ripple.core.coretypes.AccountID;
import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.serialized.enums.TransactionType;
import com.ripple.core.types.known.tx.Transaction;

public class AccountDelete extends Transaction{
    public AccountDelete() {
        super(TransactionType.AccountDelete);
    }
    public boolean hasDestinationTag() {return has(UInt32.DestinationTag);}
    public UInt32 destinationTag() {return get(UInt32.DestinationTag);}
    public void destinationTag(UInt32 val) { put(UInt32.DestinationTag, val);}
    public AccountID destination() {return get(AccountID.Destination);}
    public void destination(AccountID val) { put(AccountID.Destination, val);}
}
