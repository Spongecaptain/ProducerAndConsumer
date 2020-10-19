package cool.spongecaptain.blockqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @program: producerandconsumer
 * @description: PC 的含义为 Producer and Consumer,
 * 此生产者消费者模型基于 JUC 包下的 BlockingQueue 实现
 * @author: Spongecaptain
 */
public class BlockingQueuePC {

    public static void main(String[] args) {
        //队列大小限制在 100
        final int QUEUE_SIZE = 100;
        final BlockingQueue<String> taskQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);


        Runnable producerRunnable = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    taskQueue.offer("string");//生产一个新的资源加入任务队列，队列满时阻塞直至队列有空闲空间
                }
            }

            ;
        };

        Runnable consumerRunnable = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        String string = taskQueue.take();//消费队列中的一个资源，队列空时阻塞直至队列满
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
