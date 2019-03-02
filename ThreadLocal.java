package mine;

import java.lang.ref.WeakReference;

public class ThreadLocal<T> {
	public ThreadLocal() {
	}
	
	ThreadLocalMap getMap(Thread t) {
		return t.threadLocals;
	}

	public T get() {
		Thread t = Thread.currentThread();
		ThreadLocalMap map = getMap(t);
		if (map != null) {
			ThreadLocalMap.Entry e = map.getEntry(this);
			if (e != null)
				return (T) e.value;
		}
		return setInitialValue();
	}
	
	private T setInitialValue() {
		T value = initialValue();
		Thread t = Thread.currentThread();
		ThreadLocalMap map = getMap(t);
		if (map != null)
			map.set(this, value);
		else
			createMap(t, value);
		return value;
	}
	
	protected T initialValue() {
        return null;
    }
	
	static class ThreadLocalMap {
		static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
		/**
         * The initial capacity -- MUST be a power of two.
         */
        private static final int INITIAL_CAPACITY = 16;

        /**
         * The table, resized as necessary.
         * table.length MUST always be a power of two.
         */
        private Entry[] table;

        /**
         * The number of entries in the table.
         */
        private int size = 0;
        
        private Entry getEntry(ThreadLocal<?> key) {
            return null;
        }


		
	}

}
