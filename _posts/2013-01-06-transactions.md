---
layout: default
title: Fault Tolerant Code Generation Using Transactions
shortTitle: Transactions
documentationExpanded: false
comments: true
postsExpanded: true
excerpt: A short time ago a bug posted on the Dagger issue list prompted an architectural rework in the way Transfuse performs analysis and code generation.  This was a significant change which resulted in a very resilient and fascinating design with some positive side effects.  The following covers the core concepts and results of the change in hopes that it will provide some guidance for implementing Annotation Processors.
root: ../../../
---

### Transfuse Technical Blog

#### Fault Tolerant Code Generation Using Transactions
January 6, 2013

A short time ago a [bug posted on the Dagger issue list][1] prompted an [architectural rework][3] in the way Transfuse performs analysis and code generation.  This was a significant change which resulted in a very resilient and fascinating design with some positive side effects.  The following covers the core concepts and results of the change in hopes that it will provide some guidance for implementing Annotation Processors.

##### Basics

Transfuse implements the JSR-269 Annotation Processor standard.  An Annotation Processor can be viewed as a function, taking annotated Java code as input and producing Java classes and resources as output.  What must not be overlooked is an Annotation Processor may be running in an ecosystem of Processors, each of which may be producing code that may be input for other Annotation Processors.  One Processor's output could be the input for another Processor.

Each Annotation Processor performs its work in rounds.  Work can be defined as analysis of classes under compilation, code generation, validation, etc.  A round is a single call to `Processor.process()` with a set of Elements earmarked for the given Processor.  During compilation, Java calls the `Processor.process()` on a registered Processor until there exists a round with both no input Elements, and no code or resources were written.  A final round is called with `RoundEnvironment.processingOver() == true` for any error handling or cleanup that may be required.  This round is known as the 'last round.'

##### The ErrorType

What happens when an Annotation Processor encounters an instance whose class is not defined?  The `Processor.process()` method is passed an `ErrorType` Element.  This can be viewed as an indication of one of two problems.  Either the developer has mistyped / not implemented the given class, or another Processor which may be running is actively implementing this class.  As we cannot know the difference, we have to assume that the class will be implemented in a subsequent round.  Therefore, when an `ErrorType` is encountered, the related work must be rolled back and retried in the next round.

To retry, the given `ErrorType` Element must be reloaded using the [Elements][2] utility class:
{% highlight java %}
TypeElement reloaded = elements.getTypeElement(element.asType().toString());
{% endhighlight %}

Also, between rounds the input elements must be stored as they will not be provided again via `Processor.process()` input.

##### The Transaction Pattern

Transfuse implements this rollback / retry technique using the Transaction pattern.  Transactions wrap a unit of code analysis and generation with a try catch block, catching a `TransactionRuntimeException`.  If an `ErrorType` is encountered a `TransactionRuntimeException` is thrown so the unit may be canceled before any code is written:

{% highlight java %}
public class TransactionWorker {
    //...
    public R run(V value) {
        try {
            R result = scoped.run(value);
            complete = true;
            return result;
        } catch (TransactionRuntimeException re) {
            //retry later
            complete = false;
        } 
        return null;
    }
}
{% endhighlight %}

Conveniently, in Transfuse each Element is converted to an AST Adapter class using the `ASTTypeBuilderVisitor`.  Therefore, each Element `ErrorType` is identified early and handled in one place.  Visiting the `ErrorType` simply throws the `TransactionRuntimeException`:

{% highlight java %}
public class ASTTypeBuilderVisitor extends SimpleTypeVisitor6<ASTType, Void> {
    //...
    public ASTType visitError(ErrorType errorType, Void v) {
        throw new TransactionRuntimeException(...);
    }
}
{% endhighlight %}

A second place where the `ErrorType` creeps up, is in annotation class parameters:

{% highlight java %}
@Bind(type = BaseType.class, to = ToBeGeneratedImpl.class)
{% endhighlight %}

Again, Transfuse uses a Visitor to resolve this type, so for the annotation `ErrorType` a `TransactionRuntimeException` is thrown early and in one place.  Surprisingly, the `ErrorType` for annotations is represented as the String value `"<error>"`:

{% highlight java %}
public class AnnotationValueConverterVisitor<T> extends SimpleAnnotationValueVisitor6<T, Void> {
    private static final String ERROR_TYPE = "<error>";
    //...
    public T visitString(String value, Void aVoid) {
        //...
        if (value.equals(ERROR_TYPE)){
            throw new TransactionRuntimeException(...);
        }
        return null;
    }
}
{% endhighlight %}

Worth noting, if any code is written via the `Filer` before a Transaction is canceled, the code will persist and may cause problems retrying the given Transaction.  Transfuse avoids this problem through its use of the [CodeModel][4] library.  CodeModel allows Transfuse to queue code during a Transaction without writing it out via the `Filer`.  Only after all analysis and code generation is complete will Transfuse write CodeModel's queued code to disk via the `Filer`.  This is analogous to a commit of a Database Transaction.

During each round, Transfuse wraps input Elements in a Transaction execution class and stores them in a collection.  Transfuse executes the collection of Transactions and the completion status related to a Transaction is stored.  If a Transaction did not complete, it will simply be retried as a member of the collection in the next round.  If there exists incomplete Transactions during the last round, Transfuse will throw the exceptions associated with the failed Transactions.

##### Deeper Down the Rabbit Hole

The core concept of the Transactional Processing has been covered, but there are some larger issues to consider when implementing Annotation Processors.  Specifically, if a set of Transactions depends on another, processing of one set of Transactions must wait for another set to complete.  For example, Transfuse is configured using a class annotated with `@TransfuseModule`.  All Component processing must wait on the Modules to be processed. If there is an error in the Module processing, Component processing should not start as there will be incomplete configuration information.  Transfuse solves this problem by wrapping Transactions with logical Processors.  Processors come in a couple of different styles.  Processors may wrap a set of Transactions (`TransactionProcessorPool`), wrap a set of Processors to be executed in parallel (`TransactionProcessorComposite`), ensure one Processor executes fully before another (`TransactionProcessorChain`), or pass one Processor's aggregate input/output set into a second Processor after completing (`TransactionProcessorChannel`).  These Processors combine to give Transfuse the following powerful Processor chain:

<img src="../../../images/transfuse_processors.png"/>

In this diagram, each rectangle represents a Processor and each arrow represents the dependency relationship between Processors.  The Processor on the pointer end of the arrow waits for the Processor on the opposite side to finish before executing.

The beauty of this approach is in the API.  Transfuse has to merely add the Elements to the appropriate processor (`@TransfuseModule` into the Module Processor, `@Activity` into the Components Processor, etc) and hit `execute()`.  The Processor chain contains the logic necessary to ensure the order of processing is respected.

##### Serendipity

Due to this change in Transfuse, a couple of unexpected benefits have emerged. First and foremost, because each Transaction is essentially self-contained, the Transactions can be run in their own threads.  In fact, a Transaction class is an extension of the `Runnable` interface and executed via the `ExecutionService`.  This was more an issue of discipline than performance, as running them in their own threads requires them to be side-effect free and thread safe.

Second, partitioning the code into separate Processors decoupled the code generation facilities.  The result of this was the `@Parcel` Processor is implemented in its own Annotation Processor and a candidate for its own library (`org.androidtransfuse.ParcelAnnotationProcessor`).


##### Conclusion

Developing Annotation Processors is a challenge due to a number of factors.  This technique is becoming popular due to the performance aspects, especially in the Android space.  The Transfuse library found some positive gains in implementing fault tolerance by a Transactional model, which resulted in resilient code and a great execution model.  The hope is that this post sheds some light on one of the more interesting concepts behind the Transfuse Annotation Processor so that other Annotation Processor implementations may take advantage of this technique.
    

[1]: https://github.com/square/dagger/issues/108
[2]: http://docs.oracle.com/javase/6/docs/api/javax/lang/model/util/Elements.html
[3]: https://github.com/johncarl81/transfuse/issues/30
[4]: http://codemodel.java.net/
