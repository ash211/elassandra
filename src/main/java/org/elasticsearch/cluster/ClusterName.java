/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.cluster;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;
import org.elasticsearch.common.settings.Settings;

/**
 *
 */
public class ClusterName implements Streamable {

    public static final String SETTING = "cluster.name";

    public static final ClusterName DEFAULT = new ClusterName("elasticsearch".intern());

    private String value;

    public static ClusterName clusterNameFromSettings(Settings settings) {
    	/*
    	if (settings.getAsBoolean("data.node", false)) {
    		 try {
    			 // use reflexion to avoid dependency to cassandra when using the client API
    	         Class klass = Class.forName("org.apache.cassandra.config.DatabaseDescriptor");
    	         Method m = klass.getMethod("getClusterName");
    	         return new ClusterName( (String) m.invoke(null) );
    	     } catch (Exception e) {
    	         throw new AssertionError(e);
    	     }
    	} 
    	*/
    	return new ClusterName(settings.get("cluster.name", ClusterName.DEFAULT.value()));
    }

    private ClusterName() {

    }

    public ClusterName(String value) {
        this.value = value.intern();
    }

    public String value() {
        return this.value;
    }

    public static ClusterName readClusterName(StreamInput in) throws IOException {
        ClusterName clusterName = new ClusterName();
        clusterName.readFrom(in);
        return clusterName;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        value = in.readString().intern();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClusterName that = (ClusterName) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Cluster [" + value + "]";
    }
}