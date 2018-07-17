package com.deere.tools.spring;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Source;

import org.springframework.oxm.GenericUnmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.mime.MimeContainer;
import org.springframework.oxm.mime.MimeUnmarshaller;

/**
 * When you've got several classes, which are annotated with {@link XmlRootElement} the additional ones will be
 * deserialized as {@link JAXBElement} of the annotated type. Spring will get confused in this case, so we unwrap the
 * instance and are able to use the {@link XmlRootElement} as it was meant to be.
 *
 * This was developed to fit {@link Jaxb2Marshaller}, so expect it to fail with other classes
 *
 * @param <T>
 *            type of unmarshaller.
 */
public class UnmarshallerDecorator<T extends MimeUnmarshaller & GenericUnmarshaller>
		implements MimeUnmarshaller, GenericUnmarshaller {

	private final T decorated;

	public UnmarshallerDecorator(final T decorated) {
		this.decorated = decorated;
	}

	@Override
	public Object unmarshal(final Source source) throws IOException {

		final Object result = decorated.unmarshal(source);

		return unwrapResult(result);
	}

	@Override
	public Object unmarshal(final Source source, final MimeContainer mimeContainer) throws IOException {

		final Object result = decorated.unmarshal(source, mimeContainer);

		return unwrapResult(result);
	}

	private Object unwrapResult(final Object result) {
		if (result instanceof JAXBElement<?>) {
			return ((JAXBElement<?>) result).getValue();
		} else {
			return result;
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return decorated.supports(clazz);
	}

	@Override
	public boolean supports(final Type genericType) {
		return decorated.supports(genericType);
	}

}
