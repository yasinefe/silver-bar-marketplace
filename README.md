# Silver Bar Marketplace

This is an implementation of Silver Bar Marketplace test

## To Compile

- Please use Java 8 or newer version.
- Please enable Annotation Processor in IDE

## To Run Tests

- Following command can be run in root directory of the project

./gradlew clean test

## Comments

- As this will be used as a dependent library in WEB/UI, I did not create an API interface such as REST APIs.
- InMemoryOrderRepository has been provided but as this will be a library adding flexibility to provide different repository implementation could be useful, because of that by default InMemoryOrderRepository is initialised but can be overridden.
- createWithLineListOrderSummaryRenderer provided to create LiveOrderBoard as simple as possible, but it can be extended with createWithOrderSummaryRenderer and withOrderRepository. This approach is used to provide flexible library to apply Open Close Principle. So LiveOrderBoard can be create easily or it can be extended with new renderer and repository implementation.
- I added order type as a parameter to render the order summary as it was not clear whether summary should be listed buy and sell orders together or not.
- I did LiveOrderBoard and OrderSummaryRenderer a generic typed class as renderer could be implemented to render in different format. For example renderer could return just a list of order summary object as it is used in test, or renderer can return XML document or xml as string or JSON object or JSON as string.
- LineListOrderSummaryRenderer uses big integer of big decimal as the example does not show numbers after decimal points in test document.
- I would add BDD style test using cucumber or similar test framework, but mostly we use BDD style tests for real services which implements user stories as this is a library it might be enough to have unit tests and some integration tests.
- I did not do any logging, logging could be added.
