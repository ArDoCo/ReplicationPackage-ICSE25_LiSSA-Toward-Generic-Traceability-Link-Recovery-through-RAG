/**
 * Media Store V3
 * Copyright (C) 2015 Software Design and Quality Group (SDQ), KIT, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.ipd.sdq.mediastore.ejb.cache;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import edu.kit.ipd.sdq.mediastore.basic.config.GlobalConstantsContainer;
import edu.kit.ipd.sdq.mediastore.basic.data.AudioFile;

/**
 * Session Bean implementation class CacheSingleton
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
@LocalBean
public class CacheSingleton {

    private Cache<IdAndBitrate, AudioFile> cache;

    private int hits = 0;
    private int misses = 0;

    synchronized void incHits() {
        this.hits++;
    }

    synchronized void incMisses() {
        this.misses++;
    }

    synchronized double getHitrate() {
        return ((double) this.hits) / (this.hits + this.misses);
    }

    @PostConstruct
    public void init() {
        System.out.println("init cache");
        this.cache = CacheBuilder.newBuilder().maximumSize(GlobalConstantsContainer.getCacheCapacity()).build();
    }

    public AudioFile getIfPresent(final IdAndBitrate request) {
        final AudioFile cachedAudio = this.cache.getIfPresent(request);
        if (cachedAudio == null) {
            this.incMisses();
//			System.out.println("cache miss for (" + id + "," + bitrate + ')');
        } else {
            this.incHits();
            System.out.println("cache hit for (" + request.getId() + "," + request.getBitrate()
                    + ") Current hitrate is " + this.getHitrate());
        }
        return cachedAudio;
    }

    public void put(final IdAndBitrate idAndBitrate, final AudioFile unCachedAudio) {
        this.cache.put(idAndBitrate, unCachedAudio);
//		System.out.println("store in cache: (" + id + "," + bitrate + ')');
    }

    public void clear() {
        System.out.println("clearing cache");
        this.cache.invalidateAll();
        this.cache.cleanUp();
    }
}
