@Experimental
Feature: Retry Test
  Set test.maxRerunCount=4 to try out the retry functionality.
  You can also run the ensureNoTestFailures gradle task to fail the build even if 
  all scenarios eventually pass due to retries.
  This is useful because it helps demonstrate whether or not a failure is consistent.

  Background: 
    When step always passes

  Scenario: Passing Example
    When you fail 1 times then pass with key Passing
    When step always passes

  Scenario Outline: Outline Example
    When you fail <times> times then pass with key <key>
    When step always passes

    Examples: 
      | times | key         |
      | 0     | Pass        |
      | 1     | Fail Once   |
      | 2     | Fail Twice  |
      | 3     | Fail thrice |
