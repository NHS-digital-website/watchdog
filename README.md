# Watchdog
A Production Monitor for digital.nhs.uk, that is watching out for real-world misconfigurations.

This repository aims to safeguard the continued reliability, performance, and security of digital.nhs.uk as it interacts with real-world users. 

To ensure the highest level of service, digital.nhs.uk's codebase is thoroughly tested at every stage, aiming to catch issues early. For example, in controlled testing environments, we use unit tests, automatic continuous integration tests, and functional acceptance tests, and that’s just to name a few of our methods. However, over the years, odd issues have arisen in production that could have only arisen under real-world conditions, such as DNS network misconfigurations. 

The critical aspects of this repository are that it’s lightweight (it gives production a minimal extra workload), the tests are of real-world environmental situations only (the tests couldn’t be carried out earlier in the pipeline), and its system agnostic (it doesn’t know about production’s software and hardware).

# First time setup
You will need Java 8 or above installed on your machine, and you will need to install [Maven](https://maven.apache.org/install.html), if you don't already have them.

Once you have the above installed, execute this Maven command in the directory where your project's POM file is located.
```bash
mvn dependency:resolve
```
This will resolve the project's depenances.

# Running Watchdog
To run Watchdog you simply need to run the following command:
```bash
# For Production
mvn test -Dgroups='production,none()'

# For UAT
mvn test -Dgroups='uat,none()'
```
Note `none()` is a special tag that will run non tagged tests (i.e. @Test without @Production or @Uat).

# Adding a new test
To add a new test, create a method and annotated with the `@Test` annotation (see [J Unit](https://junit.org/junit5/docs/current/user-guide/#writing-tests) for details). If the test is only for a specific environment tag it with `@Production` or `@Uat` respectively. For example:

```java
package uk.nhs.england;

import org.junit.jupiter.api.Test;
import uk.nhs.england.tags.Production;
import uk.nhs.england.tags.Uat;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class HelloWorldTest {

    @Test @Uat // only UAT
    public void productionShouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test @Production // only Production
    public void uatShouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test // either UAT or Production
    public void alwaysShouldAnswerWithTrue() {
        assertTrue(true);
    }

}
```

# Expected output
This project essentially runs a series of J Unit tests. The output should look something like this:

```bash
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running uk.nhs.england.HelloWorldTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.063 s -- in uk.nhs.england.HelloWorldTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.792 s
[INFO] Finished at: 2023-09-13T10:27:13+01:00
[INFO] ------------------------------------------------------------------------
```

And will gernate the flowing reports:
- Placeholder
- Placeholder