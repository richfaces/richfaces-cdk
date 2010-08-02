import java.util.Iterator;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javax.xml.namespace.NamespaceContext;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * @author Nick Belaevski
 * 
 */
class SchemaProcessorNamespaceContext implements NamespaceContext {
	private BiMap<String, String> biMap;
	
	SchemaProcessorNamespaceContext() {
		biMap = HashBiMap.create();
		biMap.put("http://www.w3.org/2001/XMLSchema", "xs");
		biMap.put("http://richfaces.org/cdk/additional-attributes-info", "cdk-schema-info");
	}
	
	@Override
	public Iterator getPrefixes(String namespaceURI) {
		String prefix = biMap.get(namespaceURI);
		if (prefix != null) {
			return Iterators.forArray(prefix);
		} else {
			return Iterators.emptyIterator();
		}
	}
	
	@Override
	public String getPrefix(String namespaceURI) {
		return biMap.get(namespaceURI);
	}
	
	@Override
	public String getNamespaceURI(String prefix) {
		return biMap.inverse().get(prefix);
	}
}
