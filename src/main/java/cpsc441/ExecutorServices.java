package cpsc441;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by keynan on 18-Sep-14.
 */
public class ExecutorServices {

    public static void main(String[] args) {
        //burstyExample();
        resultExample();
    }

    public static void simpleExample() {
        // First create a thread pool in the form of an executor
        ExecutorService single = Executors.newSingleThreadExecutor();

        //Do some work
        single.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world!");
            }
        });

        // note that when single drops out of scope the GC is free to clean it up.
        // the finalize method will take care of the thread, provided it's not running.
    }

    ////////////////////////////////////////////////////////////////

    public static void resultExample() {
        ExecutorService several = Executors.newFixedThreadPool(4);
        Future<Integer>[] partialResults = new Future[100];

        for (int i = 0; i < 100; i++) {
            // the java memory model requires variables to be final
            // in order to garentee optimizations don't effect behavour
            // when closing over, or sharing across threads.
            final int k = i;

            Callable<Integer> job = new Callable<Integer>() {
                public Integer call() {
                    return k * 2;
                }
            };

            partialResults[i] = several.submit(job);
        }

        for (int i = 0; i < 100; i++) {
            try {
                System.out.println(partialResults[i].get(3, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {     // how might we handle these errors?
            } catch (TimeoutException e) {

            }
        }
    }

    ////////////////////////////////////////////////////////////////

    private static final Random r = new Random();
    private static class BusyWork implements Runnable {

        int limit;
        ExecutorService exec;

        public BusyWork(ExecutorService exec, int limit) {
            this.exec = exec;
            this.limit = limit;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " " +limit);
                Thread.sleep(900 + r.nextInt(100));
                if(limit > 0) {
                    for (int i = 0; i < r.nextInt(limit); i++) {
                        final int k = i;
                        exec.execute(new BusyWork(exec, k));
                    }
                }
            }catch (InterruptedException e) {
            }
        }
    }

    public static void burstyExample() {
        ExecutorService bursty = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            final int k = i;
            bursty.execute(new BusyWork(bursty, k));
        }

    }

    ////////////////////////////////////////////////////////////////

    public static void customExample() {
        // First create a thread pool in the form of an executor
        int corePoolSize = 1;
        int maxPoolSize = 1;
        int keepAlive = 0;
        TimeUnit timeUnit = TimeUnit.DAYS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();

        ExecutorService single = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, timeUnit, workQueue);

        //Do some work
        single.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world!");
            }
        });

        // note that when single drops out of scope the GC is free to clean it up.
        // the finalize method will take care of the thread, provided it's not running.
    }

}

