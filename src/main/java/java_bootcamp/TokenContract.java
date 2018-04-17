package java_bootcamp;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireThat;

/* Our contract, governing how our state will evolve over time.
 * See src/main/kotlin/examples/ExampleContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "java_bootcamp.TokenContract";

    private static void throwException(String str) {
        throw new IllegalArgumentException(str);
    }

    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        Command cmd = tx.getCommand(0);
        CommandData cmdType = cmd.getValue();

        if (cmdType instanceof Issue) {
            if (tx.getInputs().size() > 0) throwException("Input should be empty");
            if (tx.getOutputs().size() != 1) throwException("Transaction should generate output");

            TokenState outputState = (TokenState) tx.getOutputStates().get(0);

            if (outputState.getAmount() < 0) throwException("Amount should be > 0");
            if (!cmd.getSigners().contains(outputState.getOwner().getOwningKey()))
                throwException("Only Owner should Issue token");
        } else if (cmdType instanceof Transfer) {
            if (tx.getInputs().size() != 1) throwException("Input shouldn't be empty");
            if (tx.getOutputs().size() != 1) throwException("Transaction should generate output");

            TokenState outputState = (TokenState) tx.getOutputStates().get(0);
            if (outputState.getAmount() < 0) throwException("Amount should be Positive ");
            // check for signers
        } else {
            throwException("Invalid command");
        }
    }

    public static class Issue implements CommandData {
    }

    public static class Transfer implements CommandData {
    }


}