package com.accelerator.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        System.err.println("FAILED: " + result.getMethod().getQualifiedName());
        if (result.getThrowable() != null)
            result.getThrowable().printStackTrace(System.err);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIPPED: " + result.getMethod().getQualifiedName());
    }
}
