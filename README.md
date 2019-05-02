# ED-Beershop

This is a beershop that some developers started implementing.
The developers didn't get far yet, but implemented some limited use cases as fast as they could. 

But even after their limited amount of work it already became clear to them that the codebase is starting to become less maintainable.

# Your mission

You as a consultant are hired on this project to make sure the project doesn't turn into an even bigger mess,
and refactor the code so the team can keep on implementing new features without slowing down after each release.

Apply the things you have learned in the Accelerator trainings. 

# First steps

A few pointers to help you get started:

1. Create DTO's / Resources for all REST endpoints so that your domain isn't coupled to the rest api.
2. Make sure there is no application or business logic in the REST controllers
3. Decouple your domain from the DB. That means no JPA annotations in your domain model


After this you are free to progress as you see fit. Try to apply the SOLID principles, find Aggregates,
define Value Objects, decouple different domains from each other, think about dependencies between these domains,
and dependencies between components.