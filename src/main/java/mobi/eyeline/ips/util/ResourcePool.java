package mobi.eyeline.ips.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Fixed resource pool implementation.
 */
public abstract class ResourcePool<T> {

  private final BlockingQueue<T> resources;

  public ResourcePool(final int nResources) throws Exception {
    this.resources = new ArrayBlockingQueue<T>(nResources) {{
      for (int i = 0; i < nResources; i++) {
        add(init());
      }
    }};
  }

  /**
   * Initializes guarded resource instance.
   */
  protected abstract T init() throws Exception;

  /**
   * Executes an action using one of the guarded resources, waits for availability if needed.
   *
   * @throws InterruptedException If interrupted while waiting for resource availability
   * @throws Exception            Exceptions raised in the supplied action are re-thrown.
   */
  public final <V> V execute(ResourceCallable<V, T> action) throws Exception {
    final T instance = resources.take();

    try {
      return action.call(instance);

    } finally {
      resources.put(instance);
    }
  }

  public static interface ResourceCallable<V, T> {
    V call(T resource) throws Exception;
  }
}
