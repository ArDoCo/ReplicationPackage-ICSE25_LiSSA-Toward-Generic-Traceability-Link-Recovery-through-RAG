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

import java.io.Serializable;

public class IdAndBitrate implements Serializable {

    private static final long serialVersionUID = -8701255441272247362L;

    private long id;
    private int bitrate;

    public IdAndBitrate(final long id, final int bitrate) {
        this.id = id;
        this.bitrate = bitrate;
    }

    public long getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getBitrate() {
        return this.bitrate;
    }

    public void setBitrate(final int bitrate) {
        this.bitrate = bitrate;
    }

    @Override
    public int hashCode() {
//		System.out.println("get hash");
        return (this.id + "," + this.bitrate).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
//		System.out.println("equals");
        if (!(obj instanceof IdAndBitrate)) {
            return false;
        }
        final IdAndBitrate other = (IdAndBitrate) obj;
//		System.out.println("Comparing ids " + this.id + " & " + other.id + " and bitrates " + this.bitrate + " & " + other.bitrate);
        return (this.id == other.id) && (this.bitrate == other.bitrate);
    }
}
