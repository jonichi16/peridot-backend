package com.jonichi.peridot.common.util.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Listener that registers transaction events and logs transaction lifecycle details.
 *
 * <p>This class is responsible for registering callbacks that log the various stages of a
 * transaction, including before commit, after commit, and after completion.
 * </p>
 */
@Component
public class TransactionEventListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionEventListener.class);

    /**
     * Registers a transaction synchronization callback to log events related to transaction
     * lifecycle.
     *
     * <p>This method checks if transaction synchronization is active, and if so, it registers
     * callbacks to log when a transaction is about to commit, has been committed, or has
     * completed (whether it was successful or rolled back).
     * </p>
     */
    public static void registerTransactionEvents() {
        logger.info("Transaction started...");

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager
                    .registerSynchronization(new TransactionSynchronization() {

                        @Override
                        public void beforeCommit(boolean readOnly) {
                            logger.info("Transaction is about to commit. Read-only: {}", readOnly);
                        }

                        @Override
                        public void afterCommit() {
                            logger.info("Transaction successfully committed.");
                        }

                        @Override
                        public void afterCompletion(int status) {
                            if (status == STATUS_COMMITTED) {
                                logger.info("Transaction completed successfully.");
                            } else if (status == STATUS_ROLLED_BACK) {
                                logger.info("Transaction rolled back.");
                            }
                        }
                    });
        }
    }
}
