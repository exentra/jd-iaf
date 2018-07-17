package com.deere.tools.spring;

import org.springframework.context.annotation.ComponentScan;

/**
 * Classes should be ignored during standard spring {@link ComponentScan}. Add an exclude filter to the Application
 * class:
 * 
 * <pre>
 * &#64;ComponentScan(excludeFilters = @Filter(IgnoredDuringScan.class))
 * </pre>
 * 
 */
public @interface IgnoredDuringScan {
}