Code Reviewer Feedback

I went over the code - thanks again for your effort. Unfortunately, the submission does not meet our bar for design, coding and testing at this time. Details are below.

Functionality
-------------
* App crashes with NullPointerException if phone is rotated with dialog open
* Add two payments of 0.005 - total is show as 0.01, but individual payments are both shown as 0
* Unsaved payments are lost if app is killed when in the background
* Provider and reference are mandatory - question does not specify this

Design
------
* minSdk is set to 28 - question required support for SDK 21 onwards
* User-visible strings are hardcoded in the source code ("Cash" for example), making localization difficult
* String concatenation is sometimes used (in total amount, for example) - better to use string templates
* Exceptions are being swallowed silently during load - error handling can be improved
* Serialization is being done to string and then to file - can directly write / read to file stream
* File input / output is being done from UI thread - this can cause application to not respond sometimes
* Files are not being closed on exception - use try-with-resources
* Lint warnings have not been handled - avoidable errors are not being eliminated
* Code is needlessly complicated and hard to understand, with many unnecessary abstractions - more code is not better code
