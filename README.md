## Introduction
A simple application that compares core async in ClojureScript with RxJS in Javascript for a particular <a href="http://www.jayway.com/2014/09/16/comparing-core-async-and-rx-by-example/">use case</a>. 
A lot of inspiration (and code) is taken from the <a href="https://github.com/cognitect/async-webinar">core async</a> webinar.
Thanks to <a href="http://staltz.com/">André Staltz</a> for helping out with the RxJS implementation.

## Usage

Install [Leiningen](http://leiningen.org). Then run the following from the 
project directory.

```shell
lein cljsbuild auto csp
```

This will start the auto build process. Open `index.html` in your favorite 
browser to try out the examples. You can edit `src/csp/core.cljs` and `resources/rx-example.js` in
your preferred text editor and refresh the browser to view your changes.