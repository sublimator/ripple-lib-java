package com.ripple.core.types.known.tx.txns;

import com.ripple.core.coretypes.AccountID;
import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.serialized.enums.TransactionType;
import com.ripple.core.types.known.tx.Transaction;

public class EscrowCancel extends Transaction {
    public EscrowCancel() {
        super(TransactionType.EscrowCancel);
    }

    public AccountID owner() {return get(AccountID.Owner);}
    public UInt32 offerSequence() {return get(UInt32.OfferSequence);}
    public UInt32 ticketSequence() {return get(UInt32.TicketSequence);}

    public void offerSequence(UInt32 val) { put(UInt32.OfferSequence, val);}
    public void ticketSequence(UInt32 val) { put(UInt32.TicketSequence, val);}
    public void owner(AccountID val) { put(AccountID.Owner, val);}

    public boolean hasTicketSequence() {return has(UInt32.TicketSequence);}
}
