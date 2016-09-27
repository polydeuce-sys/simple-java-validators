# Simple Java Validators
This project is really just an excuse to learn about custom Java annotations, since they seem interesting, but not
necessarily something that I would encounter "in the wild". However it also seems like a useful thing to add
to some of my projects in a very simple and isolated fashion, having an implementation which is similar enough to
the javax specification for JEE6, but which does not require running in a JEE container or depending on any large
frameworks.

## Using validators
The validators work on annotatons of bean getters. The first write of this actually operated on fields, however
if one wants to be opinionated (and perhaps we should be), this breaks encapsulation quite badly, and the
thing you really want to validate is the public presentation of the values, not their internal representation.

So to use a validator one can annotate one's bean getters:
```
public class MyConfigBean{
    private String serviceUrl;
    private double pollingFrequency;


    @Url(protocol={"https"},hostWhitelist={"myserver", "myfailover"})
    public String getServiceUrl(){
        return serviceUrl;
    }

    @DoubleRange(min=1500,max=3000)
    public Double getPollingFrequency(){
        return pollingFrequency;
    }
}
```
An annotated object can then be validated at runtime by calling
`ObjectValidator.getInstance().validate(obj)`
or, if the object inherits from `ValidatableObject` by calling the `validate()` method on the object.

Note that the framework does use reflection in order to find the `Annotation` instances attached to the methods.
This does require that the SecurityManager settings do allow for these actions.

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