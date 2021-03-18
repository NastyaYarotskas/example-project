# example-project

Found this error while trying to send a non-blocking request with a large response:

```java
        get("/non-blocking", ctx ->
                Mono.fromCallable(() -> "data/test.json")
                        .map(this::readJsonAsString)
        );
```

Stacktrace:

```
 java.lang.IllegalStateException: UT000146: HttpServerExchange cannot have both async IO resumed and dispatch() called in the same cycle
	at io.undertow.server.HttpServerExchange$WriteDispatchChannel.resumeWrites(HttpServerExchange.java:2045)
	at io.undertow.io.AsyncSenderImpl.send(AsyncSenderImpl.java:188)
	at io.jooby.internal.utow.UtowContext.send(UtowContext.java:424)
	at io.jooby.internal.utow.UtowContext.send(UtowContext.java:395)
	at io.jooby.DefaultContext.render(DefaultContext.java:441)
	at reactor.core.publisher.LambdaMonoSubscriber.onNext(LambdaMonoSubscriber.java:168)
	at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.onNext(FluxMapFuseable.java:121)
	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1637)
	at reactor.core.publisher.MonoCallable.subscribe(MonoCallable.java:61)
	at reactor.core.publisher.Mono.subscribe(Mono.java:4105)
	at reactor.core.publisher.Mono.subscribeWith(Mono.java:4211)
	at reactor.core.publisher.Mono.subscribe(Mono.java:4077)
	at reactor.core.publisher.Mono.subscribe(Mono.java:4013)
	at reactor.core.publisher.Mono.subscribe(Mono.java:3985)
	at io.jooby.internal.handler.reactive.ReactorMonoHandler.apply(ReactorMonoHandler.java:26)
	at io.jooby.internal.utow.UtowContext.detach(UtowContext.java:268)
	at io.jooby.internal.handler.DetachHandler.apply(DetachHandler.java:21)
	at io.jooby.internal.StaticRouterMatch.execute(StaticRouterMatch.java:34)
	at io.jooby.internal.utow.UtowHandler.handleRequest(UtowHandler.java:48)
	at io.undertow.server.Connectors.executeRootHandler(Connectors.java:387)
	at io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest(HttpReadListener.java:255)
	at io.undertow.server.protocol.http.HttpReadListener.handleEvent(HttpReadListener.java:136)
	at io.undertow.server.protocol.http.HttpOpenListener.handleEvent(HttpOpenListener.java:162)
	at io.undertow.server.protocol.http.HttpOpenListener.handleEvent(HttpOpenListener.java:100)
	at io.undertow.server.protocol.http.HttpOpenListener.handleEvent(HttpOpenListener.java:57)
	at org.xnio.ChannelListeners.invokeChannelListener(ChannelListeners.java:92)
	at org.xnio.ChannelListeners$10.handleEvent(ChannelListeners.java:291)
	at org.xnio.ChannelListeners$10.handleEvent(ChannelListeners.java:286)
	at org.xnio.ChannelListeners.invokeChannelListener(ChannelListeners.java:92)
	at org.xnio.nio.QueuedNioTcpServer2.acceptTask(QueuedNioTcpServer2.java:178)
	at org.xnio.nio.WorkerThread.safeRun(WorkerThread.java:612)
	at org.xnio.nio.WorkerThread.run(WorkerThread.java:479)
```

You can reproduce the error by running the application and send a request to http://localhost:9040/non-blocking
