package bgu.spl.a2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 * <p>
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {
    private AtomicInteger monitorCount;

    /**
     * VersionMonitor C-tor
     */
    public VersionMonitor() {
        monitorCount = new AtomicInteger(0);
    }

    /**
     * @return returns the current version monitor number
     */
    public int getVersion() {
        return monitorCount.get();
    }

    /**
     * increment the version monitor by one and notify all waiting threads
     */
    public synchronized void inc() {
        monitorCount.addAndGet(1);
        notifyAll();
    }

    /**
     * Wait as long as parameter version matches current version
     *
     * @param version is the version to compare to current version.
     * @throws InterruptedException
     */
    public synchronized void await(int version) throws InterruptedException {
        while (getVersion() == version) {
            wait();
        }
    }
}