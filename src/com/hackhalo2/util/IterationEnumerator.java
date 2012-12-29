package com.hackhalo2.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class IterationEnumerator<T> implements Enumeration<T>, Iterator<T> {
	private final Iterator<T> iterator;
	
	public IterationEnumerator(final Iterator<T> iterator) {
		this.iterator = iterator;
	}
	
	public IterationEnumerator(final Enumeration<T> enumeration) {
		List<T> list = new ArrayList<T>();
		
		while(enumeration.hasMoreElements()) {
			list.add(enumeration.nextElement());
		}
		
		this.iterator = list.iterator();
	}

	@Override
	public boolean hasMoreElements() {
		return this.iterator.hasNext();
	}

	@Override
	public T nextElement() {
		return this.iterator.next();
	}

	@Override
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	@Override
	public T next() {
		return this.iterator.next();
	}

	@Override
	public void remove() {
		//Do nothing, since the underlying Iterator is final
	}

}
