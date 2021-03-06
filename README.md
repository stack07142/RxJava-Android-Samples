RxJava Android Samples
======================

## Examples

1. [Background work & concurrency (using Schedulers)](#1-background-work--concurrency-using-schedulers)
2. [Accumulate calls (using buffer)](#2-accumulate-calls-using-buffer)
3. [Instant/Auto searching text listeners (using debounce)](#3-instantauto-searching-text-listeners-using-debounce)
4. [Networking with Retrofit & RxJava (using zip, flatmap)](#4-networking-with-retrofit--rxjava-using-zip-flatmap)
5. [Two-way data binding for TextViews (using PublishSubject)](#5-two-way-data-binding-for-textviews-using-publishsubject)
6. [Simple and Advanced polling (using interval and repeatWhen)](#6-simple-and-advanced-polling-using-interval-and-repeatwhen)
7. [Simple and Advanced exponential backoff (using delay and retryWhen)](#7-simple-and-advanced-exponential-backoff-using-delay-and-retrywhen)
8. [Form validation (using combineLatest)](#8-form-validation-using-combinelatest)
9. [Pseudo caching : retrieve data first from a cache, then a network call (using concat, concatEager, merge or publish)](#9-pseudo-caching--retrieve-data-first-from-a-cache-then-a-network-call-using-concat-concateager-merge-or-publish)

## Descrption

### 1. Background work & concurrency (using Schedulers)

A common requirement is to offload lengthy heavy I/O intensive operations to a background thread (non-UI thread) and feed the results back to the UI/main thread, on completion. This is a demo of how long-running operations can be offloaded to a background thread. After the operation is done, we resume back on the main thread. All using RxJava! Think of this as a replacement to AsyncTasks.

The long operation is simulated by a blocking Thread.sleep call (since this is done in a background thread, our UI is never interrupted).

To really see this example shine. Hit the button multiple times and see how the button click (which is a UI operation) is never blocked because the long operation only runs in the background.

### 2. Accumulate calls (using buffer)

This is a demo of how events can be accumulated using the "buffer" operation.

A button is provided and we accumulate the number of clicks on that button, over a span of time and then spit out the final results.

If you hit the button once, you'll get a message saying the button was hit once. If you hit it 5 times continuously within a span of 2 seconds, then you get a single log, saying you hit that button 5 times (vs 5 individual logs saying "Button hit once").

Note:

If you're looking for a more foolproof solution that accumulates "continuous" taps vs just the number of taps within a time span, look at the [EventBus Demo](https://github.com/kaushikgopal/Android-RxJava/blob/master/app/src/main/java/com/morihacky/android/rxjava/rxbus/RxBusDemo_Bottom3Fragment.java) where a combo of the `publish` and `buffer` operators is used. For a more detailed explanation, you can also have a look at this [blog post](http://blog.kaush.co/2015/01/05/debouncedbuffer-with-rxjava/).

### 3. Instant/Auto searching text listeners (using debounce)

This is a demo of how events can be swallowed in a way that only the last one is respected. A typical example of this is instant search result boxes. As you type the word "Bruce Lee", you don't want to execute searches for B, Br, Bru, Bruce, Bruce, Bruce L ... etc. But rather intelligently wait for a couple of moments, make sure the user has finished typing the whole word, and then shoot out a single call for "Bruce Lee".

As you type in the input box, it will not shoot out log messages at every single input character change, but rather only pick the lastly emitted event (i.e. input) and log that.

This is the debounce/throttleWithTimeout method in RxJava.

### 4. Networking with Retrofit & RxJava (using zip, flatmap)

[Retrofit from Square](http://square.github.io/retrofit/) is an amazing library that helps with easy networking (even if you haven't made the jump to RxJava just yet, you really should check it out). It works even better with RxJava and these are examples hitting the GitHub API, taken straight up from the android demigod-developer Jake Wharton's talk at Netflix. You can [watch the talk](https://www.youtube.com/watch?v=aEuNBk1b5OE#t=2480) at this link. Incidentally, my motivation to use RxJava was from attending this talk at Netflix.

(Note: you're most likely to hit the GitHub API quota pretty fast so send in an OAuth-token as a parameter if you want to keep running these examples often).

### 5. Two-way data binding for TextViews (using PublishSubject)

Auto-updating views are a pretty cool thing. If you've dealt with Angular JS before, they have a pretty nifty concept called "two-way data binding", so when an HTML element is bound to a model/entity object, it constantly "listens" to changes on that entity and auto-updates its state based on the model. Using the technique in this example, you could potentially use a pattern like the [Presentation View Model pattern](http://martinfowler.com/eaaDev/PresentationModel.html) with great ease.

While the example here is pretty rudimentary, the technique used to achieve the double binding using a `Publish Subject` is much more interesting.

### 6. Simple and Advanced polling (using interval and repeatWhen)

This is an example of polling using RxJava Schedulers. This is useful in cases, where you want to constantly poll a server and possibly get new data. The network call is "simulated" so it forces a delay before return a resultant string.

There are two variants for this:

1. Simple Polling: say when you want to execute a certain task every 5 seconds
2. Increasing Delayed Polling: say when you want to execute a task first in 1 second, then in 2 seconds, then 3 and so on.

The second example is basically a variant of [Exponential Backoff](https://github.com/kaushikgopal/RxJava-Android-Samples#exponential-backoff).

Instead of using a RetryWithDelay, we use a RepeatWithDelay here. To understand the difference between Retry(When) and Repeat(When) I wouuld suggest Dan's [fantastic post on the subject](http://blog.danlew.net/2016/01/25/rxjavas-repeatwhen-and-retrywhen-explained/).

An alternative approach to delayed polling without the use of `repeatWhen` would be using chained nested delay observables. See [startExecutingWithExponentialBackoffDelay in the ExponentialBackOffFragment example](https://github.com/kaushikgopal/RxJava-Android-Samples/blob/master/app/src/main/java/com/morihacky/android/rxjava/fragments/ExponentialBackoffFragment.java#L111).

### 7. Simple and Advanced exponential backoff (using delay and retryWhen)
TBD

### 8. Form validation (using [`.combineLatest`](http://reactivex.io/documentation/operators/combinelatest.html))

`.combineLatest` allows you to monitor the state of multiple observables at once compactly at a single location. The example demonstrated shows how you can use `.combineLatest` to validate a basic form. There are 3 primary inputs for this form to be considered "valid" (an email, a password and a number). The form will turn valid (the text below turns blue :P) once all the inputs are valid. If they are not, an error is shown against the invalid inputs.

We have 3 independent observables that track the text/input changes for each of the form fields (RxAndroid's `WidgetObservable` comes in handy to monitor the text changes). After an event change is noticed from **all** 3 inputs, the result is "combined" and the form is evaluated for validity.

Note that the `Func3` function that checks for validity, kicks in only after ALL 3 inputs have received a text change event.

The value of this technique becomes more apparent when you have more number of input fields in a form. Handling it otherwise with a bunch of booleans makes the code cluttered and kind of difficult to follow. But using `.combineLatest` all that logic is concentrated in a nice compact block of code (I still use booleans but that was to make the example more readable).

### 9. Pseudo caching : retrieve data first from a cache, then a network call (using concat, concatEager, merge or publish)

We have two source Observables: a disk (fast) cache and a network (fresh) call. Typically the disk Observable is much faster than the network Observable. But in order to demonstrate the working, we've also used a fake "slower" disk cache just to see how the operators behave.

This is demonstrated using 4 techniques:

1. [`.concat`](http://reactivex.io/documentation/operators/concat.html)
2. [`.concatEager`](http://reactivex.io/RxJava/javadoc/rx/Observable.html#concatEager(java.lang.Iterable))
3. [`.merge`](http://reactivex.io/documentation/operators/merge.html)
4. [`.publish`](http://reactivex.io/RxJava/javadoc/rx/Observable.html#publish(rx.functions.Func1)) selector + merge + takeUntil

The 4th technique is probably what you want to use eventually but it's interesting to go through the progression of techniques, to understand why.

`concat` is great. It retrieves information from the first Observable (disk cache in our case) and then the subsequent network Observable. Since the disk cache is presumably faster, all appears well and the disk cache is loaded up fast, and once the network call finishes we swap out the "fresh" results.

The problem with `concat` is that the subsequent observable doesn't even start until the first Observable completes. That can be a problem. We want all observables to start simultaneously but produce the results in a way we expect. Thankfully RxJava introduced `concatEager` which does exactly that. It starts both observables but buffers the result from the latter one until the former Observable finishes. This is a completely viable option.

Sometimes though, you just want to start showing the results immediately. Assuming the first observable (for some strange reason) takes really long to run through all its items, even if the first few items from the second observable have come down the wire it will forcibly be queued. You don't necessarily want to "wait" on any Observable. In these situations, we could use the `merge` operator. It interleaves items as they are emitted. This works great and starts to spit out the results as soon as they're shown.

Similar to the `concat` operator, if your first Observable is always faster than the second Observable you won't run into any problems. However the problem with `merge` is: if for some strange reason an item is emitted by the cache or slower observable *after* the newer/fresher observable, it will overwrite the newer content. Click the "MERGE (SLOWER DISK)" button in the example to see this problem in action. @JakeWharton and @swankjesse contributions go to 0! In the real world this could be bad, as it would mean the fresh data would get overridden by stale disk data.

To solve this problem you can use merge in combination with the super nifty `publish` operator which takes in a "selector". I wrote about this usage in a [blog post](http://blog.kaush.co/2015/01/21/rxjava-tip-for-the-day-share-publish-refcount-and-all-that-jazz/) but I have [Jedi JW](https://twitter.com/JakeWharton/status/786363146990649345) to thank for reminding of this technique. We `publish` the network observable and provide it a selector which starts emitting from the disk cache, up until the point that the network observable starts emitting. Once the network observable starts emitting, it ignores all results from the disk observable. This is perfect and handles any problems we might have.

Previously, I was using the `merge` operator but overcoming the problem of results being overwritten by monitoring the "resultAge". See the old `PseudoCacheMergeFragment` example if you're curious to see this old implementation.

