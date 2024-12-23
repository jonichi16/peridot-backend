package com.jonichi.peridot.common.util.listener;

import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class to handle running code within a transaction context.
 *
 * <p>The {@code TransactionalHandler} class provides functionality to run operations within
 * a transactional boundary. It ensures that the provided runnable is executed in a Spring-managed
 * transaction with the appropriate propagation behavior.</p>
 */
@Service
public class TransactionalHandler {

    /**
     * Runs the provided {@code Runnable} in a transaction.
     *
     * <p>This method ensures that the {@code Runnable} is executed within a Spring transaction.
     * The transaction is started with {@code REQUIRED} propagation, meaning it will join an
     * existing transaction if one exists, or create a new transaction if none exists.</p>
     *
     * @param runnable the operation to run within the transaction context
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void runInTransaction(Runnable runnable) {
        TransactionEventListener.registerTransactionEvents();
        runnable.run();
    }

    /**
     * Executes the provided {@link Supplier} within a transactional context.
     *
     * <p>This method ensures that the code inside the {@link Supplier#get()} is executed within
     * a Spring-managed transaction. The method wraps the execution of the supplier and ensures
     * that transaction events are registered through
     * {@link TransactionEventListener#registerTransactionEvents()}. The result of the
     * {@link Supplier#get()} is returned to the caller.</p>
     *
     * <p>The default propagation level is {@link Propagation#REQUIRED}, meaning that if a
     * transaction already exists, it will be used; otherwise, a new transaction will be
     * created.</p>
     *
     * @param <T> the type of the result returned by the supplier
     * @param supplier the {@link Supplier} to execute within the transaction
     * @return the result of the {@link Supplier#get()} method
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public <T> T runInTransactionSupplier(Supplier<T> supplier) {
        TransactionEventListener.registerTransactionEvents();
        return supplier.get();
    }

}
