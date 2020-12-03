package com.ripple.core.types.known.sle.entries;

import com.ripple.core.coretypes.AccountID;
import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.coretypes.uint.UInt64;
import com.ripple.core.serialized.enums.LedgerEntryType;

public class Ticket extends IndexedLedgerEntry {
    public Ticket() {
        super(LedgerEntryType.Ticket);
    }

    public AccountID account() {return get(AccountID.Account);}
    public UInt32 expiration() {return get(UInt32.Expiration);}
    public UInt64 ownerNode() {return get(UInt64.OwnerNode);}

    public void ownerNode(UInt64 val) { put(UInt64.OwnerNode, val);}

    public UInt32 ticketSequence() {return get(UInt32.TicketSequence);}
    public void ticketSequence(UInt32 val) { put(UInt32.TicketSequence, val);}

}
