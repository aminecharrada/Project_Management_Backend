package com.projectmanagement.ProjectManagement.service;

public class CustomOptimisticLockingFailureException extends RuntimeException {
    public CustomOptimisticLockingFailureException(String message) {
        super(message);
    }
}
