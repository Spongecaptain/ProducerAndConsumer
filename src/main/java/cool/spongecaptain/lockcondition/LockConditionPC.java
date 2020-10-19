package cool.spongecaptain.lockcondition;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: producerandconsumer
 * @description: PC 的含义为 Producer and Consumer,
 * 此生产者消费者模型基于 Condition 以及 JUC 包下的 Lock 实现
 * @author: Spongecaptain
 */
public class LockConditionPC {
    public static void main(String[] args) {
        //队列大小限制在 100
        final int QUEUE_SIZE = 100;
        ArrayDeque<String> taskQueue = new ArrayDeque<>(QUEUE_SIZE);
        //创建锁资源
        Lock lock = new ReentrantLock();
        //创建两个 Condition 实例，分别代表队列不同的状态条件：非满与非空
        final Condition notFull = lock.newCondition();
        final Condition notEmpty = lock.newCondition();

        Runnable producerRunnable = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    lock.lock();//获得锁资源
                    try {
                        while (taskQueue.size()==QUEUE_SIZE) {
                            try {
                                notFull.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        if (taskQueue.size()==100||!taskQueue.add("string")){
                            System.out.println("已经满了还试图添加资源");
                        }
                        notEmpty.await();//此时队列不为空，唤醒等待资源的消费者线程
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.lock();//必须要在 finally 语句块中释放资源，为了即使是出现异常，也能正常释放锁资源，避免影响其他线程
                    }
                }
            }
        };

        Runnable consumerRunnable = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    lock.lock();//获得锁资源
                    try{
                        while(taskQueue.isEmpty()){
                            try{
                                notEmpty.await();
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                        String string = taskQueue.getFirst();//生产一个新的资源加入任务队列
                        notFull.await();//此时队列不为满，唤醒等待队列空闲位置的生产者线程
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally{
                        lock.lock();//必须要在 finally 语句块中释放资源，为了即使是出现异常，也能正常释放锁资源，避免影响其他线程
                    }
                }
            }
        };

        Thread p1 = new Thread(producerRunnable);
        Thread p2 = new Thread(producerRunnable);
        Thread p3 = new Thread(producerRunnable);
        Thread p4 = new Thread(producerRunnable);

        Thread c1 = new Thread(consumerRunnable);
        Thread c2 = new Thread(consumerRunnable);

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        c1.start();
        c2.start();

    }


}
