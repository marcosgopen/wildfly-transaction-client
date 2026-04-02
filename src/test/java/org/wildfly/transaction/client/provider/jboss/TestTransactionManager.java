/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2026 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.transaction.client.provider.jboss;

import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class TestTransactionManager implements TransactionManager {

    TestTransaction current;

    public static boolean committed = false;
    public static boolean rolledback = false;

    @Override
    public void begin() throws NotSupportedException, SystemException {
        current = new TestTransaction();
    }

    @Override
    public void commit() throws SecurityException, IllegalStateException, SystemException {
        current.commit();
        committed = true;
    }

    @Override
    public int getStatus() throws SystemException {
        return current.getStatus();
    }

    @Override
    public Transaction getTransaction() throws SystemException {
        return current;
    }

    @Override
    public void resume(Transaction obj) throws InvalidTransactionException, IllegalStateException, SystemException {
        ((TestTransaction) obj).resume();
        current = (TestTransaction) obj;
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        current.rollback();
        rolledback = true;
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        current.setRollbackOnly();
    }

    @Override
    public void setTransactionTimeout(int seconds) throws SystemException {

    }

    @Override
    public Transaction suspend() throws SystemException {
        if (current == null) {
            return null;
        }
        TestTransaction t = current;
        current = null;
        t.suspend();
        return t;
    }

    public static void reset() {
        committed = false;
        rolledback = false;
    }
}
