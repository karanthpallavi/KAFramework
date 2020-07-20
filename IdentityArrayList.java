/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semdistance;

/**
 *
 * @author DrKM
 */
import java.util.ArrayList;
import java.util.Collection;
 
/**
* An array list implemention depending on object identity (==) rather than
* equality (.equals) to identify elements.<br/>
* <br/>
* See: <a href=
* "https://nexnet.wordpress.com/2011/03/09/java-list-equality-and-object-identity/"
* >KnowledgeNetworks: Java List Equality and Object Identity</a>
*
* @author <a href="http://www.mxro.de/">Max Rohde</a>
*
* @param <E>
*/
public class IdentityArrayList<E> extends ArrayList<E> {
 
private static final long serialVersionUID = 1L;
 
@Override
public boolean remove(final Object o) {
return super.remove(this.indexOf(o)) != null;
}
 
@Override
public boolean contains(final Object o) {
return indexOf(o) >= 0;
}
 
@Override
public int indexOf(final Object o) {
for (int i = 0; i < size(); i++)
if (o == get(i))
return i;
return -1;
}
 
@Override
public int lastIndexOf(final Object o) {
for (int i = size() - 1; i >= 0; i--)
if (o == get(i))
return i;
return -1;
}
 
public IdentityArrayList() {
super();
}
 
public IdentityArrayList(final Collection<? extends E> c) {
super(c);
}
 
public IdentityArrayList(final int initialCapacity) {
super(initialCapacity);
}
 
}