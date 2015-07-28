package cn.studease.util.cache;

/**
 * Author: liushaoping
 * Date: 2015/7/28.
 */
public class CacheKeyBuilder {

    private String prefix = "";

    public CacheKeyBuilder(String prefix) {
        this.prefix = prefix;
    }

    public CacheKey build(String key) {
        return new CacheKey(String.format("%s_%s", prefix, key));
    }

    public class CacheKey {

        private String key;

        private CacheKey() {
        }

        private CacheKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return key;
        }
    }

}
