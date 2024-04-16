// Code generated by gobind. DO NOT EDIT.

// Java class io.nekohasekai.libbox.AndroidVPNType is a proxy for talking to a Go program.
//
//   autogenerated by gobind -lang=java -javapkg=io.nekohasekai github.com/sagernet/sing-box/experimental/libbox
package io.nekohasekai.libbox;

import go.Seq;

public final class AndroidVPNType implements Seq.Proxy {
	static { Libbox.touch(); }
	
	public final int refnum;
	
	@Override public final int incRefnum() {
	      Seq.incGoRef(refnum, this);
	      return refnum;
	}
	
	AndroidVPNType(int refnum) { this.refnum = refnum; Seq.trackGoRef(refnum, this); }
	
	public AndroidVPNType() { this.refnum = __New(); Seq.trackGoRef(refnum, this); }
	
	private static native int __New();
	
	public final native String getCoreType();
	public final native void setCoreType(String v);
	
	public final native String getCorePath();
	public final native void setCorePath(String v);
	
	public final native String getGoVersion();
	public final native void setGoVersion(String v);
	
	@Override public boolean equals(Object o) {
		if (o == null || !(o instanceof AndroidVPNType)) {
		    return false;
		}
		AndroidVPNType that = (AndroidVPNType)o;
		String thisCoreType = getCoreType();
		String thatCoreType = that.getCoreType();
		if (thisCoreType == null) {
			if (thatCoreType != null) {
			    return false;
			}
		} else if (!thisCoreType.equals(thatCoreType)) {
		    return false;
		}
		String thisCorePath = getCorePath();
		String thatCorePath = that.getCorePath();
		if (thisCorePath == null) {
			if (thatCorePath != null) {
			    return false;
			}
		} else if (!thisCorePath.equals(thatCorePath)) {
		    return false;
		}
		String thisGoVersion = getGoVersion();
		String thatGoVersion = that.getGoVersion();
		if (thisGoVersion == null) {
			if (thatGoVersion != null) {
			    return false;
			}
		} else if (!thisGoVersion.equals(thatGoVersion)) {
		    return false;
		}
		return true;
	}
	
	@Override public int hashCode() {
	    return java.util.Arrays.hashCode(new Object[] {getCoreType(), getCorePath(), getGoVersion()});
	}
	
	@Override public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("AndroidVPNType").append("{");
		b.append("CoreType:").append(getCoreType()).append(",");
		b.append("CorePath:").append(getCorePath()).append(",");
		b.append("GoVersion:").append(getGoVersion()).append(",");
		return b.append("}").toString();
	}
}
