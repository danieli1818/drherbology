package drherbology.utils;

import java.beans.PropertyChangeListener;

public interface PropertyChangeObservable {

	public boolean addPropertyChangeListener(PropertyChangeListener listener);
	
	public boolean removePropertyChangeListener(PropertyChangeListener listener);
	
}
