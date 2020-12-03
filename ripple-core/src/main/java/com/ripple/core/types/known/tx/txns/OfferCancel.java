package com.ripple.core.types.known.tx.txns;

import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.serialized.enums.TransactionType;
import com.ripple.core.types.known.tx.Transaction;

public class OfferCancel extends Transaction {
    public OfferCancel() {
        super(TransactionType.OfferCancel);
    }
    public UInt32 offerSequence() {return get(UInt32.OfferSequence);}
    public void offerSequence(UInt32 val) { put(UInt32.OfferSequence, val);}

    public boolean hasTicketSequence() {return has(UInt32.TicketSequence);}
    public UInt32 ticketSequence() {return get(UInt32.TicketSequence);}
    public void ticketSequence(UInt32 val) { put(UInt32.TicketSequence, val);}
}
