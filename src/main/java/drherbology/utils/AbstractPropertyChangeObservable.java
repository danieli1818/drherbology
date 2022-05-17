package drherbology.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

public class AbstractPropertyChangeObservable implements PropertyChangeObservable {
	
	private PropertyChangeSupport pcSupport;
	
	public AbstractPropertyChangeObservable() {
		this.pcSupport = new PropertyChangeSupport(this);
	}

	@Override
	public boolean addPropertyChangeListener(PropertyChangeListener listener) {
		pcSupport.addPropertyChangeListener(listener);
		return true;
	}

	@Override
	public boolean removePropertyChangeListener(PropertyChangeListener listener) {
		if (Arrays.asList(pcSupport.getPropertyChangeListeners()).contains(listener)) {
			pcSupport.removePropertyChangeListener(listener);
			return true;
		}
		return false;
	}
	
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		pcSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
		pcSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		pcSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	protected void firePropertyChange(PropertyChangeEvent pcEvent) {
		pcSupport.firePropertyChange(pcEvent);
	}

}
