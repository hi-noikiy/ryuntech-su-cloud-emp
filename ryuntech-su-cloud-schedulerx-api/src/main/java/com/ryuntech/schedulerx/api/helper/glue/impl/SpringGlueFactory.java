package com.ryuntech.schedulerx.api.helper.glue.impl;

import com.ryuntech.schedulerx.api.helper.executor.impl.XxlJobSpringExecutor;
import com.ryuntech.schedulerx.api.helper.glue.GlueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static com.ryuntech.schedulerx.api.helper.executor.impl.XxlJobSpringExecutor.getApplicationContext;

/**
 * @author xuxueli 2018-11-01
 */
public class SpringGlueFactory extends GlueFactory {
    private static Logger logger = LoggerFactory.getLogger(SpringGlueFactory.class);


    /**
     * inject action of spring
     * @param instance
     */
    @Override
    public void injectService(Object instance){
        if (instance==null) {
            return;
        }

        if (getApplicationContext() == null) {
            return;
        }

        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Object fieldBean = null;
            // with bean-id, bean could be found by both @Resource and @Autowired, or bean could only be found by @Autowired

            if (AnnotationUtils.getAnnotation(field, Resource.class) != null) {
                try {
                    Resource resource = AnnotationUtils.getAnnotation(field, Resource.class);
                    if (resource.name()!=null && resource.name().length()>0){
                        fieldBean = getApplicationContext().getBean(resource.name());
                    } else {
                        fieldBean = getApplicationContext().getBean(field.getName());
                    }
                } catch (Exception e) {
                }
                if (fieldBean==null ) {
                    fieldBean = getApplicationContext().getBean(field.getType());
                }
            } else if (AnnotationUtils.getAnnotation(field, Autowired.class) != null) {
                Qualifier qualifier = AnnotationUtils.getAnnotation(field, Qualifier.class);
                if (qualifier!=null && qualifier.value()!=null && qualifier.value().length()>0) {
                    fieldBean = getApplicationContext().getBean(qualifier.value());
                } else {
                    fieldBean = getApplicationContext().getBean(field.getType());
                }
            }

            if (fieldBean!=null) {
                field.setAccessible(true);
                try {
                    field.set(instance, fieldBean);
                } catch (IllegalArgumentException e) {
                    logger.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

}
