package com.medguard.exception;

/**
 * Wraps low-level SQLExceptions thrown inside DAO classes so the
 * controller/service layers don't need to know JDBC details.
 * (An unchecked exception keeps method signatures clean.)
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
