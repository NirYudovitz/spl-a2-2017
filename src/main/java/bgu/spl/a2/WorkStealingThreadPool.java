package bgu.spl.a2;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {
    private ConcurrentLinkedDeque<Task<?>>[] tasksQueues;
    private Processor[] ProcessorsArr;
    private Thread[] threadsArr;
    private int nthreads;
    private VersionMonitor verMonitor;

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     * <p>
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     *                 thread pool
     */
    public WorkStealingThreadPool(int nthreads) {
        this.nthreads = nthreads;
        tasksQueues = new ConcurrentLinkedDeque[nthreads];
        threadsArr = new Thread[nthreads];
        verMonitor = new VersionMonitor();
        ProcessorsArr = new Processor[nthreads];
        for (int j = 0; j < nthreads; j++) {
            ProcessorsArr[j] = new Processor(j, this);
            threadsArr[j] = new Thread(ProcessorsArr[j]);
            tasksQueues[j] = new ConcurrentLinkedDeque<>();
        }

    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {
        Random rnd = new Random();
        int executeProc = rnd.nextInt(nthreads);
        tasksQueues[executeProc].addLast(task);
        verMonitor.inc();
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     * <p>
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException          if the thread that shut down the threads is
     *                                       interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     *                                       shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {
        int id = 0;
        for (Thread t : threadsArr) {
            if (Thread.currentThread().getId() == t.getId()) {
                throw new UnsupportedOperationException();
            }
            ProcessorsArr[id].setIsWorking(false);
            t.interrupt();
            id++;
        }
        for (Thread t : threadsArr) {
            if (Thread.currentThread().getId() == t.getId()) {
                throw new UnsupportedOperationException();
            }
            t.join();
        }
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for (Thread t : threadsArr) {
            t.start();
        }
    }

    public Task<?> getNextTask(int id) {
            return (tasksQueues[id].pollFirst());
    }

    /**
     * Stealing half tasks from other processor.
     *
     * @param id id the processor that steals.
     * @return True if stolen task,False otherwise.
     */
    public boolean stealTasks(int id) {
        boolean stolen = false;
        int counter = 1;
        AtomicInteger currentVerMonitor = new AtomicInteger(verMonitor.getVersion());
        while (!stolen) {
            int currentIdProc = (id + counter) % nthreads;
            if (!(tasksQueues[currentIdProc].isEmpty())) {
                int numStealTasks = ((tasksQueues[currentIdProc].size()) / 2);
                if (numStealTasks == 0) {
                    stolen = steal(id, currentIdProc);
                } else {
                    int i = 0;
                    for (; i < numStealTasks; i++) {
                        if (!steal(id, currentIdProc)) {
                            break;
                        }
                    }
                    if (i > 0) {
                        stolen = true;
                    }

                }
            }
            counter++;
            if (counter == tasksQueues.length) {
                if (!(currentVerMonitor.get() == verMonitor.getVersion())) {
                    counter = 1;
                    continue;
                }
                break;
            }

        }
        return stolen;

    }

    /**
     * @param id is the id of proccesor to check if have tasks.
     * @return True if the proccessor have tasks,False otherwise.
     */
    public boolean haveTasks(int id) {
        return !(tasksQueues[id].isEmpty());
    }

    /**
     * @param from is the id of the the processor to steal from.
     * @param to   is the id of the processor that steal.
     * @return false if there is no more task to steal , true if steal was success.
     */
    private boolean steal(int to, int from) {
        Task<?> taskToSteal = tasksQueues[from].pollLast();
        if (taskToSteal != null) {
            return tasksQueues[to].add(taskToSteal);
        }
        return false;
    }

    /**
     * adding tasks to proccessor.
     *
     * @param id   is the id of proccesor to check if have tasks.
     * @param task is the tasks to be added to proccessor.
     */
    public void addTasksToProcessor(int id, Task<?>... task) {
        for (Task<?> t : task) {
            tasksQueues[id].add(t);
        }
        verMonitor.inc();
    }

    /**
     * adding one task to proccessor.
     *
     * @param id   is the id of proccesor to check if have tasks.
     * @param task is the task to be added to proccessor.
     */
    public void addOneTaskToProcessor(int id, Task<?> task) {
        tasksQueues[id].add(task);
        verMonitor.inc();
    }

    /**
     * @return the current version monitor of the pool.
     */
    public VersionMonitor getVersionMonitor() {
        return verMonitor;
    }

}
