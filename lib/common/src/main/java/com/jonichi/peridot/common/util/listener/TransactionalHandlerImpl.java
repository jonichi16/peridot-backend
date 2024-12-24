package com.jonichi.peridot.common.util.listener;

import com.jonichi.peridot.common.util.TransactionalHandler;
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
public class TransactionalHandlerImpl implements TransactionalHandler {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void runInTransaction(Runnable runnable) {
        TransactionEventListener.registerTransactionEvents();
        runnable.run();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public <T> T runInTransactionSupplier(Supplier<T> supplier) {
        TransactionEventListener.registerTransactionEvents();
        return supplier.get();
    }

}
