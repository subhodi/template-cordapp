package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

/* Our flow, automating the process of updating the ledger.
 * See src/main/java/examples/IAmAFlowPair.java for an example. */
@InitiatingFlow
@StartableByRPC
public class TokenFlow extends FlowLogic<SignedTransaction> {
    private final ProgressTracker progressTracker = new ProgressTracker();
    private final Party recipient;
    private final int amount;

    public TokenFlow(Party recipient, int amount) {
        this.recipient = recipient;
        this.amount = amount;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // We choose our transaction's notary (the notary prevents double-spends).
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        Party issuer = getOurIdentity();


        // We build our transaction.
        TransactionBuilder transactionBuilder = new TransactionBuilder(notary); // TODO: Build a valid transaction.

        transactionBuilder.addOutputState(new TokenState(issuer, recipient,1), TokenContract.ID);

        transactionBuilder.addCommand(new TokenContract.Issue(),getOurIdentity().getOwningKey());
        // We check our transaction is valid based on its contracts.
        transactionBuilder.verify(getServiceHub());

        // We sign the transaction with our public key, making it immutable.
        SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(transactionBuilder);

        // We get the transaction notarised and recorded automatically by the platform.
        return subFlow(new FinalityFlow(signedTransaction));
    }
}