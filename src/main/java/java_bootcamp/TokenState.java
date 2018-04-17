package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/* Our state, defining a shared fact on the ledger.
 * See src/main/kotlin/examples/IAmAState.java and
 * src/main/kotlin/examples/IAmAlsoAState.java for examples. */
public class TokenState implements ContractState {
    private Party owner;

    private Party newOwner;
    private final int amount;

    public TokenState(Party owner, Party newOwner, int amount) {
        this.owner = owner;
        this.newOwner = newOwner;
        this.amount = amount;
    }

    public Party getNewOwner() {
        return newOwner;
    }

    public Party getOwner() {
        return owner;
    }

    public int getAmount() {
        return amount;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner, newOwner);
    }

}