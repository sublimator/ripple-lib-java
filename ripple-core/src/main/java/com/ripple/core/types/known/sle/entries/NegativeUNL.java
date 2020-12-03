package com.ripple.core.types.known.sle.entries;

import com.ripple.core.coretypes.Blob;
import com.ripple.core.coretypes.STArray;
import com.ripple.core.serialized.enums.LedgerEntryType;
import com.ripple.core.types.known.sle.LedgerEntry;

public class NegativeUNL extends LedgerEntry  {
    public NegativeUNL() {
        super(LedgerEntryType.NegativeUNL);
    }

    public boolean hasValidatorToDisable() {return has(Blob.ValidatorToDisable);}
    public Blob validatorToDisable() {return get(Blob.ValidatorToDisable);}
    public void validatorToDisable(Blob val) { put(Blob.ValidatorToDisable, val);}

    public boolean hasValidatorToReEnable() {return has(Blob.ValidatorToReEnable);}
    public Blob validatorToReEnable() {return get(Blob.ValidatorToReEnable);}
    public void validatorToReEnable(Blob val) { put(Blob.ValidatorToReEnable, val);}

    public boolean hasDisabledValidators() {return has(STArray.DisabledValidators);}
    public STArray disabledValidators() {return get(STArray.DisabledValidators);}
    public void disabledValidators(STArray val) { put(STArray.DisabledValidators, val);}
}
