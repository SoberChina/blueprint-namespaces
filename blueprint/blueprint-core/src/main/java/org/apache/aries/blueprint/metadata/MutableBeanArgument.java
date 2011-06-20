package org.apache.aries.blueprint.metadata;

import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.Target;

public interface MutableBeanArgument<T extends BeanArgument> extends BeanArgument, MutableMetadata<T> {
	MutableBeanArgument<T> value(Target value);
	MutableBeanArgument<T> valueType(String type);
	MutableBeanArgument<T> index(int index);
}
