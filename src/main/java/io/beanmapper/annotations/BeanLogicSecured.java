package io.beanmapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a field or method to be secured. BeanMapper will query the RoleSecuredCheck
 * to see if the active user is allowed to access the field.
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanLogicSecured {

    /**
     * The handler that verifies whether access to the property is allowed
     * @return the handler that takes care of the allowance verification
     */
    Class<? extends LogicSecuredCheck> value();

}
