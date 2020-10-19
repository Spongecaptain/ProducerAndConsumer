# README

这是一个 Java 语言下生产者与消费者模型，分别使用了：

- JUC 包下的 BlockingQueue 阻塞队列；
- Condition 以及 JUC 包下的 Lock；
- JUC 包下的 Semaphore 类；
- Object#wait 以及 Object#notifyAll 方法

每一种实现方式都支持多生成者-多消费者模型。