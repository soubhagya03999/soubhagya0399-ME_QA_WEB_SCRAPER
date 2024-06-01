package demo;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenersClass implements ITestListener{
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("************"+"StartTestCase:"+result.getName()+"************");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("************"+"EndTestCase"+result.getName()+"************");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("************"+"FailTestCase"+result.getName()+"************");
    }
}
