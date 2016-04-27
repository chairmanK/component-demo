# component-demo

This example application demonstrates usage of
[component](https://stuartsierra/component).

## Usage

To run the application:

    lein trampoline run -p $PORT -d $DATABASE

where `$PORT` is the listening port number for the HTTP server and `$DATABASE`
is the filesystem path to a H2 database file (which will be created if it does
not already exist)

To run tests:

    lein test

To start a REPL:

    lein repl

The REPL will start in a `user` namespace that is prepopulated for a
["reloaded"](https://github.com/stuartsierra/reloaded) workflow. In the REPL,
call

    user=> (go)

to reload source and restart the application.

## License

Copyright © 2016 Steve M. Kim

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
