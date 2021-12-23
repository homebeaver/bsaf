package org.jdesktop.application;

import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import org.junit.Test;


public class AbstractBeanTest {

    boolean onEdt = false;
    Object lock = new Object();
    
    @Test
    public void fireOnEdtTest() throws InterruptedException {
        TestBean bean = new TestBean();
        bean.addPropertyChangeListener(new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                synchronized (lock) {
                    onEdt = SwingUtilities.isEventDispatchThread();
                    lock.notify();
                }
            }
        });
        
        synchronized (lock) {
            bean.fire();
            lock.wait(1000);
        }
        
        assertTrue(onEdt);
    }
    
    private static final class TestBean extends AbstractBean {
        void fire() {
            firePropertyChange("test", null, null);
        }
    }
}
