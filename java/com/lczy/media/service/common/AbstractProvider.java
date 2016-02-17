/**
 * 
 */
package com.lczy.media.service.common;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Wu.Yanhong
 *
 */
public abstract class AbstractProvider implements Provider {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private AtomicBoolean loading = new AtomicBoolean();
	
	private AtomicBoolean initialized = new AtomicBoolean();
	

	/**
	 * 如果数据还没准备好就等待加载数据，否则什么也不做.
	 */
	protected void tryLoad() {
		if (!initialized.getAndSet(true)) {
			reload();
		} else if (loading.get()) {
			synchronized (this) {
				try {
					wait(30 * 1000);
				} catch (InterruptedException e) {
					log.warn("线程异常终止", e);
				}
			}
		}
	}
	
	@Override
	public synchronized void reload() {
		try {
			loading.set(true);
			log.debug("===>> Load data from database...");
			load();
		} finally {
			loading.set(false);
			notifyAll();
        }
	}

	/**
	 * 加载数据的逻辑，由子类实现.
	 */
	protected abstract void load();

	@Override
	public void clear() {}
	
	/**
	 * 标记缓存数据为脏数据.
	 */
	public void setDirtyFlag() {
		initialized.set(false);
	}

}
