package com.ripple.core.types.known.tx.txns;

import com.ripple.core.coretypes.hash.Hash256;
import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.serialized.enums.TransactionType;
import com.ripple.core.types.known.tx.Transaction;

public class CheckCancel extends Transaction {
    public CheckCancel() {
        super(TransactionType.CheckCancel);
    }

    public Hash256 checkID() {return get(Hash256.CheckID);}
    public void checkID(Hash256 val) { put(Hash256.CheckID, val);}

    public boolean hasTicketSequence() {return has(UInt32.TicketSequence);}
    public UInt32 ticketSequence() {return get(UInt32.TicketSequence);}
    public void ticketSequence(UInt32 val) { put(UInt32.TicketSequence, val);}
}
