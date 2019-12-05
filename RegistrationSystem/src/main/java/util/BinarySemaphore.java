package util;

public class BinarySemaphore {
    boolean value;

    public BinarySemaphore(boolean initValue) {
        value = initValue;
    }

    public synchronized void P() {
        while (value == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        value = false;
    }

    public synchronized void V() {
        value = true;
        notify();
    }
}
