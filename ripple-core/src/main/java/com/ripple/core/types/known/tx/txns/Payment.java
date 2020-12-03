package com.ripple.core.types.known.tx.txns;

import com.ripple.core.coretypes.AccountID;
import com.ripple.core.coretypes.Amount;
import com.ripple.core.coretypes.PathSet;
import com.ripple.core.coretypes.hash.Hash256;
import com.ripple.core.coretypes.uint.UInt32;
import com.ripple.core.fields.Field;
import com.ripple.core.serialized.enums.TransactionType;
import com.ripple.core.types.known.tx.Transaction;

public class Payment extends Transaction {
    public Payment() {
        super(TransactionType.Payment);
    }

    public UInt32 destinationTag() {return get(UInt32.DestinationTag);}
    public Hash256 invoiceID() {return get(Hash256.InvoiceID);}
    public Amount amount() {return get(Amount.Amount);}
    public Amount sendMax() {return get(Amount.SendMax);}
    public AccountID destination() {return get(AccountID.Destination);}
    public UInt32 ticketSequence() {return get(UInt32.TicketSequence);}

    public PathSet paths() {return get(PathSet.Paths);}
    public Amount deliverMin() {return get(Amount.DeliverMin);}

    public void destinationTag(UInt32 val) {put(Field.DestinationTag, val);}
    public void invoiceID(Hash256 val) {put(Field.InvoiceID, val);}
    public void deliverMin(Amount val) { put(Amount.DeliverMin, val);}
    public void amount(Amount val) {put(Field.Amount, val);}
    public void sendMax(Amount val) {put(Field.SendMax, val);}
    public void destination(AccountID val) {put(Field.Destination, val);}
    public void paths(PathSet val) {put(Field.Paths, val);}
    public void ticketSequence(UInt32 val) { put(UInt32.TicketSequence, val);}

    public boolean hasDestinationTag() {return has(UInt32.DestinationTag);}
    public boolean hasInvoiceID() {return has(Hash256.InvoiceID);}
    public boolean hasSendMax() {return has(Amount.SendMax);}
    public boolean hasDeliverMin() {return has(Amount.DeliverMin);}
    public boolean hasTicketSequence() {return has(UInt32.TicketSequence);}
}
