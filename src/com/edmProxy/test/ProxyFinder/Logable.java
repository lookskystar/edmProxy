
/**
 * @author 李晓宇， hmily_yu@hotmail.com
 * Logable接口提供一般的记录信息功能
 */
package com.edmProxy.test.ProxyFinder;

public interface Logable {
    public void addLog(String log);

    public void addResult(String log);

    public void setStatus();

    public boolean getFlag();
}
