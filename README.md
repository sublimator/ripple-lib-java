The Ripple Java Library [![CircleCI](https://circleci.com/gh/sublimator/ripple-lib-java.svg?style=svg)](https://circleci.com/gh/sublimator/ripple-lib-java) [![codecov](https://codecov.io/gh/sublimator/ripple-lib-java/branch/master/graph/badge.svg)](https://codecov.io/gh/sublimator/ripple-lib-java) [![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=sublimator/ripple-lib-java)](https://dependabot.com)

===============

# THIS LIBRARY HAS NOT BEEN USED IN PRODUCTION FOR PROCESSING REAL MONEY. DO NOT USE UNLESS YOU KNOW WHAT YOU ARE DOING.

Yes, `ripple-lib-java` is Java code for working with... ripple.

I (@sublimator) wrote this haphazardly over the years while working at ripple,
firstly as a weekend project to learn the protocol, and given my acquaintance,
it was my goto tool when processing millions of old transactions, looking for
transactions that violated implicit invariants. The nodejs ripple-lib has very
slow binary support, and no out of the box threading.

The Java client is crude, and the async model used is basically a port of nodejs
EventEmitter, with `on(key, callback)` handlers, and keeping to a single thread.
It is not very a nice API, and could do with modernizing to use RxJava etc.

Likewise, the Transaction/LedgerEntry objects are merely wrappers around
TreeMap<Field, SerializedType> and don't make good use of libraries like
Jackson to auto populate fields.

See: [ripple-lib-java-sucks](https://github.com/sublimator/ripple-lib-java-sucks)

### Current status

  - Binary serialization/parsing/shamap
  - Crude implementation of a high level client
    - Single threaded
    - High level helper classes
      - AccountTxPager (wraps account_tx)
      - PaymentFlow (wraps path_find)
    - Automatic transaction resubmission
      - Resubmits transactions in manner resilient to poor network conditions
        - !!!NEEDS TESTING AGAINST LATEST RIPPLED!!!
  - Api client choice of WebSocket transport
  - Test suite for core types
  - Signing / Verification
  - KeyPair creation

### Docs

  - [ShaMap](ripple-core/src/main/java/com/ripple/core/types/shamap/README.md)
  - [Serialized Types](ripple-core/README.md)
  - [Transaction Manager](ripple-client/src/main/java/com/ripple/client/transactions/README.md)

### TODO
  - See the issues!
  - Documentation
  - Complete test coverage

### Examples

  - See in ripple-examples/ folder
