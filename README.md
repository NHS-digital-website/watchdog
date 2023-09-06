# Watchdog
A Production Monitor for digital.nhs.uk, that is watching out for real-world misconfigurations.

This repository aims to safeguard the continued reliability, performance, and security of digital.nhs.uk as it interacts with real-world users. 

To ensure the highest level of service, digital.nhs.uk's codebase is thoroughly tested at every stage, aiming to catch issues early. For example, in controlled testing environments, we use unit tests, automatic continuous integration tests, and functional acceptance tests, and that’s just to name a few of our methods. However, over the years, odd issues have arisen in production that could have only arisen under real-world conditions, such as DNS network misconfigurations. 

The critical aspects of this repository are that it’s lightweight (it gives production a minimal extra workload), the tests are of real-world environmental situations only (the tests couldn’t be carried out earlier in the pipeline), and its system agnostic (it doesn’t know about production’s software and hardware).

