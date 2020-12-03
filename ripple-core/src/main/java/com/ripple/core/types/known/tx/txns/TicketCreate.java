package com.ripple.core.types.known.tx.txns;

import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.serialized.enums.TransactionType;
import com.ripple.core.types.known.tx.Transaction;

public class TicketCreate extends Transaction {
    public TicketCreate() {
        super(TransactionType.TicketCreate);
    }

    public void expiration(UInt32 val) { put(UInt32.Expiration, val);}

    public UInt32 ticketCount() {return get(UInt32.TicketCount);}
    public void ticketCount(UInt32 val) { put(UInt32.TicketCount, val);}

    public boolean hasTicketSequence() {return has(UInt32.TicketSequence);}
    public UInt32 ticketSequence() {return get(UInt32.TicketSequence);}
    public void ticketSequence(UInt32 val) { put(UInt32.TicketSequence, val);}
}
