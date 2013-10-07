package jo.sm.ui.act.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Description {
    String displayName() default "";
    String shortDescription() default "";
    String documentation() default "";
    int priority() default 50;
}
