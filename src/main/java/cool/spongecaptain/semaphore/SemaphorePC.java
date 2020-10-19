package cool.spongecaptain.semaphore;

import java.util.ArrayDeque;
import java.util.concurrent.Semaphore;

/**
 * @program: producerandconsumer
 * @description: PC 的含义为 Producer and Consumer,
 * 启动对多生产者消费者用于证明算法不仅仅支持一个生产者与消费者模型
 * 如果并发出现问题,那么控制台会输出"已经满了还试图添加资源",这说明此模型不支持多生产者-多消费者模型
 * 此生产者消费者模型基于 java.util.concurrent.Semaphore 实现
 * 以及 Object#notifyAll 方法实现
 * @author: Spongecaptain
 */
public class SemaphorePC {

    public static void main(String[] args) {
        //队列大小限制在 100
        final int QUEUE_SIZE = 100;
        ArrayDeque<String> taskqueue = new ArrayDeque<>(QUEUE_SIZE);
        //创建三个信号量
        final Semaphore notFull = new Semaphore(QUEUE_SIZE);//这里的大小小于等于队列大小
        final Semaphore notEmpty = new Semaphore(0);
        final Semaphore mutex = new Semaphore(1);

        Runnable producerRunnable = new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        notFull.acquire();//在队列资源满时阻塞
                        mutex.acquire();//获得起到锁资源作用的 Semaphore 实例资源：

                        if (taskqueue.size()==QUEUE_SIZE||!taskqueue.add("string")){
                            System.out.println("已经满了还试图添加资源");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mutex.release();//释放 mutex 资源
                        notEmpty.release();//为 notEmpty 中新增一个 permit
                    }
                }
            }
        };

        Runnable consumerRunnable = new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    try {
                        notEmpty.acquire();//在队列资源空时阻塞
                        mutex.acquire();//获得起到锁资源作用的 Semaphore 实例资源：
                        String string = taskqueue.pollFirst();//得到生产队列中的一个资源
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mutex.release();//释放 mutex 资源
                        notFull.release();//为 notFull 中新增一个 permit
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
