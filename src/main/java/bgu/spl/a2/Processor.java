package bgu.spl.a2;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class Processor implements Runnable {
    AtomicBoolean isWorking;
    private final WorkStealingThreadPool pool;
    private final int id;

    /**
     * constructor for this class
     * <p>
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     * <p>
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id   - the processor id (every processor need to have its own unique
     *             id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        isWorking = new AtomicBoolean(true);
        this.id = id;
        this.pool = pool;
    }

    /**
     * checks if tasks are available:
     * if so - handle them
     * if not - try to steal from the other processors
     * if can't steal - go to sleep until new tasks are available
     */
    @Override
    public void run() {
        while (isWorking.get()) {
            if (pool.haveTasks(id)) {
                Task t = pool.getNextTask(id);
                // Check if stolen.
                if (t != null) {
                    t.handle(this);
                }
            } else {
                boolean successSteal = pool.stealTasks(id);
                if (!successSteal) {
                    try {
                        pool.getVersionMonitor().await(pool.getVersionMonitor().getVersion());
                    } catch (InterruptedException ix) {
                        //continue loop.
                    }
                }
            }
        }

    }

    /**
     * add a collection of Tasks to the current processor
     *
     * @param task - tasks to be added to the processor
     */
    public void addTasks(Task<?>... task) {
        pool.addTasksToProcessor(id, task);
    }

    /**
     * add a single task to the current processor
     *
     * @param task - a task to be added to the processor
     */
    public void addOneTask(Task<?> task) {
        pool.addOneTaskToProcessor(id, task);
    }


}
