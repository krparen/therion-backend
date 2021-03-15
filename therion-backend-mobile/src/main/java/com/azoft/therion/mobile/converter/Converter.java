package com.azoft.therion.mobile.converter;

public interface Converter<I, O> {
  O convert (I input);
}
