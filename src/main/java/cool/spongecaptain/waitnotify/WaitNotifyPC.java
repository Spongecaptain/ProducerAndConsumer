package cool.spongecaptain.waitnotify;

import java.util.ArrayDeque;

/**
 * @program: producerandconsumer
 * @description: PC 的含义为 Producer and Consumer,
 * 此生产者消费者模型基于 Object#wait 以及 Object#notifyAll 方法实现
 * @author: Spongecaptain
 */
public class WaitNotifyPC {
    public static void main(String[] args) {
        //队列大小限制在 100
        final int QUEUE_SIZE = 100;
        ArrayDeque<String> taskQueue = new ArrayDeque<>(QUEUE_SIZE);
        //创建一个锁对象
        final Object LOCK = new Object();

        Runnable producerRunnable = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    //1. 先得到互斥锁
                    synchronized (LOCK) {
                        //2. 检查队列是否已满
                        while (taskQueue.size() == QUEUE_SIZE) {
                            try {
                                //3-1 如若已满，那么释放锁资源，然后阻塞
                                LOCK.wait();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //3-2 如果未满，那么生产一个实例，加入队列中
                        if (taskQueue.size()==QUEUE_SIZE||!taskQueue.add("string")){
                            System.out.println("已经满了还试图添加资源");
                        }
                        //4. 唤醒其他等待任务队列资源的线程
                        LOCK.notifyAll();
                    }//5. 自动释放锁资源
                }
            }
        };

        Runnable consumerRunnable = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    synchronized (LOCK) {
                        while (taskQueue.isEmpty()) {
                            try {
                                LOCK.wait();//队列是空的，则使自己阻塞
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        String string = taskQueue.getFirst();//消费任务队列中的一个资源
                        LOCK.notifyAll();//唤醒其他等待任务队列资源的线程
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
