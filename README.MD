# Simple Java Validators
This project is really just an excuse to learn about custom Java annotations, since they seem interesting, but not
necessarily something that I would encounter "in the wild". However it also seems like a useful thing to add
to some of my projects in a very simple and isolated fashion, having an implementation which is similar enough to
the javax specification for JEE6, but which does not require running in a JEE container or depending on any large
frameworks.

## Using validators
The validators work on annotatons of bean getters. The first write of this actully operated on fields, however
if one wants to be opinionated (and perhaps we should be), this breaks encapsulation quite badly, and the
thing you really want to validate is the public presentation of the values, not thier internal representation.

So, the framework using this library can either have it's beans inherit from the `ValidatableObject` class, or
call the `ObjectValidator.getInstance().validate(obj)` method. Note that the framework does use reflection in
order to find the `Annotation` instances attached to the methods. This does require that the SecurityManager settings
do allow for these actions.

## Writing Custom Validations
Adding your own validations is fairly simple.

```java
    @Constraint(validatedBy = MyValidator.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @Inherited
    public @interface MyAnnotation{
        aType myConstraint() default myDefault
   }
```

Define your annotation, and mark the class of the constraint validation using the `@Constraint` annotation.
The additional criteria (retention, target and inherited) should be specified as above.

The simplest way to implement a validator class is to inherit from the abstract type validator class (for example
`StringValidator`, `DoubleValidator` or `LongValidator`) as these require only that you provide an implementation of
the appropriate `protected abstract boolean doApply(V v, T validationCriteria);` method with `V` being the value type
and `T` the annotation type.

Subclassing `BaseValidator` requires implementing the additional
`protected abstract V convertValueToValidatingType( Object value)` method to coerce the type of bean property
to the correct type for validation.