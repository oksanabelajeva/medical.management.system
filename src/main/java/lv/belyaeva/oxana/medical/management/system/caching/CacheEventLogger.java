package lv.belyaeva.oxana.medical.management.system.caching;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEventLogger implements CacheEventListener<Object, Object> {

    private final Logger LOG = LoggerFactory.getLogger(CacheEventLogger.class);

    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        LOG.info("Patients' old list = {}, patients' new list = {}", cacheEvent.getOldValue(), cacheEvent.getNewValue());

    }
}
